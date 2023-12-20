<?php

namespace App\Resolver;

use App\Repository\UserRepository;
use App\Repository\TicketRepository;
use App\Repository\EventRepository;
use Overblog\GraphQLBundle\Resolver\ResolverMap;
use DateTime;

class MyResolverMap extends ResolverMap
{
    private UserRepository $userRepository;
    private EventRepository $eventRepository;
    private TicketRepository $ticketRepository;

    public function __construct(UserRepository $userRepository, EventRepository $eventRepository, TicketRepository $ticketRepository)
    {
        $this->userRepository = $userRepository;
        $this->eventRepository = $eventRepository;
        $this->ticketRepository = $ticketRepository;
    }

    public function map(): array
{
    return [
        'Query' => [
            'users' => function ($rootValue, $args) {
                $email = $args['email'] ?? null;
                if ($email !== null) {
                    return $this->userRepository->findBy(['email' => $email]);
                }

                return $this->userRepository->findAll();
            },
            'ticket' => function ($rootValue, $args) {
                $id = $args['id'] ?? null;
                if ($id !== null) {
                    return $this->ticketRepository->find($id);
                }

                return null;
            },
        ],
        'User' => [
            'tickets' => function ($user) {
                return $user->getTickets();
            },
        ],
        'Ticket' => [
            'event' => function ($ticket) {
                return $this->eventRepository->find($ticket->getEvent()->getId());
            },
        ],
        'Event' => [
            'startDate' => function ($event) {
                $startDateString = $event->getStartDate();
                return $startDateString->format('Y-m-d H:i:s');
            },
            'endDate' => function ($event) {
                $endDateString = $event->getEndDate();
                return $endDateString->format('Y-m-d H:i:s');
            },
        ],
    ];
}
}
