import router from "../router"
import axios from "axios"

const onResponseSuccess = response => {
  return response
}

const onResponseError = err => {
  const status = err.status || err.response.status
  if (status === 500 || status === 401) {
    // not logged in
    router.push('/login')
  }

  if (status >= 500 || status === 400) {
    console.error('[axios-global]invalid request')
  }
  return Promise.reject(err)
}

axios.interceptors.response.use(onResponseSuccess, onResponseError)