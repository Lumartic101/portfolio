<?php

namespace App\Security;

use Lexik\Bundle\JWTAuthenticationBundle\Exception\JWTDecodeFailureException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Exception\AuthenticationException;
use Symfony\Component\Security\Core\Exception\CustomUserMessageAuthenticationException;
use Symfony\Component\Security\Http\Authenticator\AbstractAuthenticator;
use Symfony\Component\Security\Http\Authenticator\Passport\Passport;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use App\Repository\UserRepository;
use Symfony\Component\HttpFoundation\Cookie;
use Lexik\Bundle\JWTAuthenticationBundle\Services\JWTTokenManagerInterface;
use Symfony\Component\Security\Http\Authenticator\AuthenticatorInterface;
use Symfony\Component\Security\Http\Authenticator\Passport\Badge\UserBadge;
use Symfony\Component\Security\Http\Authenticator\Passport\SelfValidatingPassport;

// the appauthenticator is not getting used at the moment because of loginlinkinterface
class AppAuthenticator extends AbstractAuthenticator implements AuthenticatorInterface
{
    private const LOGIN_ROUTE = '/api/login';
    private UrlGeneratorInterface $urlGenerator;
    private JWTTokenManagerInterface $jwtManager;
    private UserRepository $userRepository;

    public function __construct(UrlGeneratorInterface $urlGenerator, UserRepository $userRepository, JWTTokenManagerInterface $jwtManager)
    {
        $this->urlGenerator = $urlGenerator;
        $this->userRepository = $userRepository;
        $this->jwtManager = $jwtManager;
    }

    public function supports(Request $request): ?bool
    {
        return ($request->getPathInfo() === self::LOGIN_ROUTE && $request->isMethod('GET'));
    }

    public function getCredentials(Request $request)
    {
        if ($request->getPathInfo() != '/api/login_check') {
            return;
        }

        $token = $request->cookies->get('AUTH_TOKEN');

        if (!$token) {
            throw new CustomUserMessageAuthenticationException('Invalid or missing JWT token.');
        }

        return $token;
    }

    public function start(Request $request)
    {
        echo 'start';
        $token = $request->cookies->get('AUTH_TOKEN');;
        echo htmlspecialchars($token,  ENT_QUOTES, 'UTF-8');

        if (!$this->isTokenValid($token)) {
            throw new AuthenticationException('Invalid token');
        }

        return null;
    }

    public function isTokenValid($token)
    {
        try {
            $decodedToken = $this->jwtManager->decode($token);
            $expirationTimestamp = $decodedToken['exp'];
            $currentTimestamp = time();

            if ($expirationTimestamp < $currentTimestamp) {
                return false; // Token has expired
            }
            return true; // Token is valid
        } catch (JWTDecodeFailureException $exception) {
            return false; // Token is invalid
        }
    }

    public function authenticate(Request $request): Passport
    {
        $token = $request->query->get('token');

        if ($token === null || $token === '') {
            throw new CustomUserMessageAuthenticationException('Token not valid');
        }

        // will not work since token does not exist anymore
        $user = $this->userRepository->findOneBy(['generatedToken' => $token]);

        if (!$user) {
            throw new CustomUserMessageAuthenticationException('User not found');
        }

        $passport = new SelfValidatingPassport(
            new UserBadge($user->getEmail()),
            // more badges can be added in this array if needed
            []
        );
        return $passport;
    }

    public function onAuthenticationSuccess(Request $request, TokenInterface $token, string $firewallName): ?Response
    {
        $user = $token->getUser();
        $jwtToken = $this->jwtManager->create($user);

        $response = new Response();

        $response->headers->setCookie(
            Cookie::create('AUTH_TOKEN', $jwtToken)
                ->withSecure(true) // Set the secure flag to only send over HTTPS
                ->withHttpOnly(true) // Set the httponly flag to prevent client-side access
                ->withSameSite(Cookie::SAMESITE_STRICT) // Set the SameSite attribute to strict
        );        

        return $response;
    }

    public function onAuthenticationFailure(Request $request, AuthenticationException $exception): ?Response
    {
        // Left empty because routing is done through vue.js
        return null;
    }

    protected function getLoginUrl(Request $request): string
    {
        return $this->urlGenerator->generate(self::LOGIN_ROUTE);
    }
}
