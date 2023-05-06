// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import {createApp} from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/globle.css'
import '@/scripts/AxiosSettings.js'
import {router} from "@/router"
import {store} from "@/scripts/GlobalStorage"

/* eslint-disable no-new */
const app = createApp(App)

app.config.productionTip = false
app.use(ElementPlus)
app.use(router)
app.use(store)

app.mount('#app')