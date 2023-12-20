<?php

namespace App\Tests\ControllerTests;

use App\Entity\User;
use PHPUnit\Framework\TestCase;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;

class EmailControllerTest extends TestCase
{
    public function testSendEmail()
    {
        $mailer = $this->createMock(\Symfony\Component\Mailer\MailerInterface::class);
        $userRepository = $this->createMock(\App\Repository\UserRepository::class);

        $userRepository->method('findOneBy')->willReturn(new User());

        $emailController = new \App\Controller\EmailController();

        $response = $emailController->sendEmail($mailer, $userRepository, 'user@gmail.com');

        $this->assertEquals(200, $response->getStatusCode());
    }

    public function testSendInvitationEmail()
    {
        $mailer = $this->createMock(\Symfony\Component\Mailer\MailerInterface::class);

        $emailController = new \App\Controller\EmailController();

        $response = $emailController->sendInvitationEmail($mailer, 'test@example.com');
        $this->assertEquals(200, $response->getStatusCode());
    }

    public function testCannotSendInvitationEmailWithNoLength()
    {
        $mailer = $this->createMock(\Symfony\Component\Mailer\MailerInterface::class);

        $emailController = new \App\Controller\EmailController();

        $response = $emailController->sendInvitationEmail($mailer, '');
        $this->assertEquals(400, $response->getStatusCode());
    }

    public function testCannotSendEmailToNotKnownUser()
    {
        $mailer = $this->createMock(\Symfony\Component\Mailer\MailerInterface::class);
        $userRepository = $this->createMock(\App\Repository\UserRepository::class);

        $userRepository->method('findOneBy')->willReturn(new User());

        $emailController = new \App\Controller\EmailController();

        $response = $emailController->sendEmail($mailer, $userRepository, 'qweqeqwe@asdasd.com');

        $this->assertEquals('', $response->getContent());
    }
}
