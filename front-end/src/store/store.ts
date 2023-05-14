import { createStore, type Store, useStore as baseUseStore } from 'vuex'
import { apiGetUserID, apiGetUserInfo } from '@/scripts/API_User'
import type { UserInfo } from '@/scripts/interfaces'
import type { InjectionKey } from 'vue'

export interface State {
  userInfo?: UserInfo
  token: string
}

export const key: InjectionKey<Store<State>> = Symbol('key for store')

export const store = createStore<State>({
  state: {
    userInfo: undefined as UserInfo | undefined,
    token: ''
  },
  mutations: {
    logout (state: State) {
      state.userInfo = undefined
      state.token = ''
    },
    setToken (state: State, token: string) {
      // token of type: "satoken: xxx"
      state.token = token.substring(token.indexOf(':') + 2)
    },
    setUserInfo (state: State, info: UserInfo) {
      state.userInfo = info
    }
  },
  actions: {
    async updateInfo (context) {
      try {
        const id = await apiGetUserID()
        const info = await apiGetUserInfo(id)
        context.commit('setUserInfo', info)
      } catch (e) {
        // didn't login
        context.commit('logout')
      }
    }
  }
})

export function useStore (): Store<State> {
  return baseUseStore(key)
}
