import { Store } from 'vuex'
import type {UserInfo} from "../scripts/interfaces";

declare module '@vue/runtime-core' {
  // declare your own store states
  interface State {
    userInfo: UserInfo
  }

  // provide typings for `this.$store`
  interface ComponentCustomProperties {
    $store: Store<State>
  }
}