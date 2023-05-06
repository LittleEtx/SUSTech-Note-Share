import Vuex from 'vuex'
import Vue from 'vue'
import {apiGetUserID} from './API_User'

Vue.use(Vuex)

export const store = new Vuex.Store({
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
