// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import {createApp} from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import '@/assets/element-variables.scss'
import '@/assets/globle.css'
import '@/scripts/AxiosSettings.js'
import {router} from "@/router"
import {store} from "@/scripts/GlobalStorage"
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

/* eslint-disable no-new */
const app = createApp(App)

app.config.productionTip = false
app.use(ElementPlus)
app.use(router)
app.use(store)

// 全局导入所有的 ElementPlus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')