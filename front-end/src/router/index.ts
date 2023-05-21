import { createRouter, createWebHashHistory } from 'vue-router'
import LoginPage from '@/pages/LoginPage.vue'
import ResetPasswordPage from '@/pages/ResetPasswordPage.vue'
import HomePage from '@/pages/HomePage.vue'
import PersonalCenterPage from '@/pages/PersonalCenterPage.vue'
import group from '@/components/group/CenterGroups.vue'
import groupShow from '@/components/group/GroupShow.vue'
import NotebookPage from '@/pages/NotebookPage.vue'
import NotFoundPage from '@/pages/NotFoundPage.vue'
import HistoryPage from '@/pages/HistoryPage.vue'

export const router = createRouter({
  history: createWebHashHistory(), // 暂时使用Hash模式，线上部署时再改成History模式
  routes: [
    {
      path: '/:pathMatch(.*)',
      component: NotFoundPage
    },
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
      path: '/notebook/:notebookID',
      name: 'notebook',
      component: NotebookPage
    },
    {
      path: '/user/:userId',
      component: PersonalCenterPage
    },
    {
      path: '/groupTest',
      component: group
    },
    {
      path: '/showTest/:groupID',
      component: groupShow
    },
    {
      path: '/history',
      name: 'history',
      component: HistoryPage
    }
  ]
})
