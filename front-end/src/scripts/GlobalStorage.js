import { createStore } from 'vuex'
import { apiGetUserID } from './API_User'

export const store = createStore({
  state: {
    userID: ''
  },
  getters: {
    userID: async state => {
      state.userID = await apiGetUserID()
      return state.userID
    }
  },
  mutations: {
    logout (state) {
      state.userID = ''
    }
  }
})
