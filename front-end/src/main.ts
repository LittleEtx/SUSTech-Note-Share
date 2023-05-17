// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import '@/assets/element-variables.scss'
import '@/assets/global.css'
import '@/scripts/AxiosSettings.js'
import { router } from './router'
import { key, store } from './store/store'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'

import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

/* eslint-disable no-new */
const app = createApp(App)

app.use(ElementPlus)
app.use(router)
app.use(store, key)
app.use(mavonEditor)

// 全局导入所有的 ElementPlus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
// 更新用户信息，供全局使用
await store.dispatch('updateInfo')
app.mount('#app')
