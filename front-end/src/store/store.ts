// @ts-ignore
import {createStore, Store, useStore as baseUseStore} from 'vuex'
import {apiGetUserID, apiGetUserInfo} from '../scripts/API_User'
import type {UserInfo} from "../scripts/interfaces";
import type {InjectionKey} from "vue";

export interface State {
  userInfo: UserInfo | {}
}

export const key: InjectionKey<Store<State>> = Symbol()

export const store = createStore<State>({
  state: {
    userInfo: {}
  },
  mutations: {
    logout (state: State) {
      state.userInfo = {}
    },
    setUserInfo (state: State, info: UserInfo) {
      state.userInfo = info
    }
  },
  actions: {
    async updateInfo (context: Store<State>) {
      try {
        const id = await apiGetUserID()
        const info = await apiGetUserInfo(id)
        context.commit('setUserInfo', info)
      } catch (e) {
        //didn't login
        context.commit('logout')
      }
    },
  }
})

export function useStore () {
  return baseUseStore(key)
}
