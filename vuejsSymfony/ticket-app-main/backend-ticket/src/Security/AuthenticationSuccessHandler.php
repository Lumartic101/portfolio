<?php

namespace App\Security;

use Psr\Log\LoggerInterface;
use Symfony\Component\HttpFoundation\Cookie;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Http\Authentication\DefaultAuthenticationSuccessHandler;
use Symfony\Component\Security\Http\HttpUtils;

// not used right now because the override does not work
class AuthenticationSuccessHandler extends DefaultAuthenticationSuccessHandler
{
    private string $frontendUrl;

    public function __construct( string $frontendUrl, HttpUtils $httpUtils, ?LoggerInterface $logger = null)
    {
        $options = [];
        parent::__construct($httpUtils, $options, $logger);

        $this->frontendUrl = $frontendUrl;
    }

    public function onAuthenticationSuccess(Request $request, TokenInterface $token): Response
    {
        $response = parent::onAuthenticationSuccess($request, $token);

        $newURL = $this->frontendUrl;

        $userEmail = $token->getUser()->getUserIdentifier();

        $cookie = new Cookie(
            'UserMail',
            $userEmail,
            time() + 10800,
            '/',
            null,
            false,
            false,
            true,
            'lax'
        );

        $response->headers->setCookie($cookie);
        return $response;
    }

}