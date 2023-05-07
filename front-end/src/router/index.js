import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/pages/LoginPage.vue'
import ResetPasswordPage from '@/pages/ResetPasswordPage.vue'
import HomePage from '@/pages/HomePage.vue'
import PersonalCenterPage from '@/pages/PersonalCenterPage.vue'

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: 'home'
    },
    {
      path: '/login',
      name: 'login',
      component: LoginPage
    },
    {
      path: '/reset_password',
      name: 'reset_password',
      component: ResetPasswordPage
    },
    {
      path: '/home',
      name: 'home',
      component: HomePage
    },
    {
      path: '/user/:userId',
      component: PersonalCenterPage
    }
  ]
})
