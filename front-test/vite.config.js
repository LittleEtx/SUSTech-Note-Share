import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 8080,
    open: true, // 在服务器启动时自动在浏览器中打开应用程序
    proxy: {
      '^/api': {
        target: 'http://localhost:8088/api/', // 后端接口的根目录
        changeOrigin: true, // 是否跨域
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
