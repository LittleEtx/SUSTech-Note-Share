import { createStore } from 'vuex'
import {apiGetUserID, apiGetUserInfo} from './API_User'

export const store = createStore({
  state: {
    userInfo: {}
  },
  mutations: {
    logout (state) {
      state.userInfo = {}
    },
    setUserInfo (state, info) {
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
        //didn't login
        context.commit('logout')
      }
    },
  }
})
