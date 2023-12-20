<?php

namespace App\Controller;

use Symfony\Component\HttpFoundation\Response;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use App\Repository\UserRepository;
use Symfony\Component\Security\Http\LoginLink\LoginLinkHandlerInterface;
use Symfony\Component\Notifier\NotifierInterface;
use Symfony\Component\Notifier\Recipient\Recipient;
use Symfony\Component\Security\Http\LoginLink\LoginLinkNotification;

final class LoginController extends AbstractController
{
    public function requestLoginLink(NotifierInterface $notifier, LoginLinkHandlerInterface $loginLinkHandler, UserRepository $userRepository, Request $request): Response
    {
        if ($request->isMethod('POST')) {
            $email = $request->query->get('email');

            try {
                $user = $userRepository->findOneBy(['email' => $email]);

                $loginLinkDetails = $loginLinkHandler->createLoginLink($user);

                $notification = new LoginLinkNotification(
                    $loginLinkDetails,
                    'Ticket login CM.com'
                );

                $recipient = new Recipient($user->getEmail());

                $notifier->send($notification, $recipient);

            } catch (\Exception $e) {
                // Do nothing in the catch block
            }
        }

        return new Response('email sent if user found', 200);
    }

}
