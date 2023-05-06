import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: 'home'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../components/LoginPage.vue')
    },
    {
      path: '/reset_password',
      name: 'reset_password',
      component: () => import('../components/ResetPasswordPage.vue')
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('../components/HomePage')
    },
    {
      path: '/user/:userId',
      component: () => import('../components/PersonalCenterPage')
    }
  ]
})
