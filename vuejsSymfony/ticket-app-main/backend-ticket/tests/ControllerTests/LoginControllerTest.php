<?php

namespace App\Tests\ControllerTests;

use Symfony\Bundle\FrameworkBundle\Test\WebTestCase;

class LoginControllerTest extends WebTestCase
{
    public function testRequestLoginLinkUserFound(): void
    {
        $client = static::createClient();

        $client->request('POST', '/login?email=abe.kasper@gmail.com');

        $this->assertSame(200, $client->getResponse()->getStatusCode());

        $content = $client->getResponse()->getContent();

        $this->assertStringContainsString('email sent if user found', $content);
    }
}