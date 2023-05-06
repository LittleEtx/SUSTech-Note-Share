import {createRouter, createWebHistory} from "vue-router"
import LoginPage from "@/components/LoginPage.vue"
import ResetPasswordPage from "@/components/ResetPasswordPage.vue"
import HomePage from "@/components/HomePage.vue"
import PersonalCenterPage from "@/components/PersonalCenterPage.vue"

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
