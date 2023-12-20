<?php

namespace App\DataFixtures;

use App\Entity\User;
use App\Entity\Ticket;
use App\Entity\Event;
use DateTime;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;

class AppFixtures extends Fixture
{
    public function load(ObjectManager $manager): void
    {
        $jsonData = file_get_contents(__DIR__ . '/../../mock-data.json');
        $data = json_decode($jsonData, true);

        foreach ($data['User'] as $userData) {
            $user = new User();
            $user->setEmail($userData['email']);

            foreach ($userData['ticket'] as $ticketData) {
                $ticket = new Ticket();
                $ticket->setHolderName($ticketData['holderName']);
                $ticket->setType($ticketData['type']);
                $ticket->setOrderId($ticketData['orderId']);
                $ticket->setPrice($ticketData['price']);

                $eventData = $ticketData['Event'];
                $event = new Event();
                $event->setLocation($eventData['location']);
                $event->setEventName($eventData['eventName']);
                $event->setCountry($eventData['country']);
                $event->setStartDate(new DateTime($eventData['startDate']));
                $event->setEndDate(new DateTime($eventData['endDate']));

                $manager->persist($event);
                $ticket->setEvent($event);
                $manager->persist($ticket);
                $user->addTicket($ticket);
            }

            $manager->persist($user);
        }

        $manager->flush();
    }
}
