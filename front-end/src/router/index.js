import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/logOn',
      name: 'logOn',
      component: () => import('../components/logOn')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../components/register')
    },
    {
      path: '/rePwd',
      name: 'rePwd',
      component: () => import('../components/rePwd')
    }
  ]
})
