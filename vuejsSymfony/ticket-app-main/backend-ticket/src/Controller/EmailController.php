<?php

// src/Controller/EmailController.php
namespace App\Controller;

use App\Resolver\MyResolverMap;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use App\Repository\UserRepository;

class EmailController extends AbstractController
{
    public function sendEmail(MailerInterface $mailer, UserRepository $userRepository, string $emailTo): Response
    {
        $user = $userRepository->findOneBy(['email' => $emailTo]);

        if(!$user){
            return new Response(null, 200);
        }

        $email = (new Email())
            ->from('testingappticket@gmail.com')
            ->to($emailTo)
            ->subject('Verification for ticket app')
            ->text('Hello, we have verified you!');

        $mailer->send($email);

        return new Response(null, 200);
    }

    /**
     * @Route("/api/send-invitation-email", name="send-invitation-email")
     */
    public function sendInvitationEmail(MailerInterface $mailer, string $emailTo)
    {
        if($emailTo === ''){
            return new Response('no email length', 400);
        }

        $email = (new Email())
            ->from('testingappticket@gmail.com')
            ->to($emailTo)
            ->subject('You have been invited to go to an event!')
            ->text('You\'ve been invited to go to an event! Click here to join!');

        $mailer->send($email);

        return new Response('successfully send email to: ' . $emailTo, 200);
    }
}