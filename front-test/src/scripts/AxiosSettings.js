import axios from 'axios'
import Cookies from 'js-cookie'
import {router} from "@/router"

const onResponseSuccess = response => {
  return response
}

const onResponseError = err => {
  const status = err.status || err.response.status
  if (status === 401 || status === 403) {
    // not logged in, remove login token
    console.error('[axios-global]invalid token')
    Cookies.remove('satoken')
    return router.push('/login')
  }

  if (status >= 500) {
    console.error('[axios-global]invalid request')
  }
  return Promise.reject(err)
}

axios.interceptors.response.use(onResponseSuccess, onResponseError)
