import {defineConfig} from "vite";

export default defineConfig({
    server: {
        proxy: {
            //配置到本地的后端接口
            '/api': {
                target: 'http://localhost:8080/api/', //接口域名
                changeOrigin: true,                   //是否跨域
                rewrite: (path) =>
                    path.replace(/^\/api/, ''), //重写接口路径
            }
        }
    }
});