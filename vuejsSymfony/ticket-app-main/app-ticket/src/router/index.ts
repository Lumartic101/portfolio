import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import NotFoundView from '@/views/NotFoundView.vue'
import SuccessView from '@/views/SuccessView.vue'
import TicketOverlayView from '@/views/TicketOverlayView.vue'
import TicketOverview from '@/views/TicketOverview.vue'
import ViewTicket from '@/views/ViewTicket.vue'
import ManageOverview from '@/views/ManageOverview.vue'
import SocialMediaOverlay from '@/views/SocialMediaOverlay.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/:catchAll(.*)',
      name: '404',
      component: NotFoundView,
    },
    {
      path: '/success',
      name: 'success',
      component: SuccessView,
    },
    {
      path: '/ticket/:id',
      name: 'ticket',
      component: TicketOverlayView,
    },
    {
      path: '/ticket-menu',
      name: 'ticket-menu',
      component: TicketOverview,
    },
    {
      path: '/tickets',
      name: 'tickets',
      component: ViewTicket,
    },
    {
      path: '/manage',
      name: 'manage',
      component: ManageOverview,
    },
    {
      path: '/manage/social',
      name: 'social',
      component: SocialMediaOverlay,
    },
  ]
})

export default router
