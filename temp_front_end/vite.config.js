import {defineConfig} from "vite";
import viteBaseConfig from "./vite.base.config";
import viteDevConfig from "./vite.dev.config";
import viteProdConfig from "./vite.prod.config";

const envResolver = {
    // build: () => ({...viteBaseConfig,... viteProdConfig}) 这种方式也可以
    // Object.assign中的{}是为了防止viteBaseConfig被修改。
    build: () => Object.assign({}, viteBaseConfig, viteProdConfig),
    serve: () => Object.assign({}, viteBaseConfig, viteDevConfig),
};
export default defineConfig(
    ({command, mode, ssrBuild}) => {
        return envResolver[command]();
    });