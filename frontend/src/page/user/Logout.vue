<template>
  <div></div>
</template>

<script>
import axios from 'axios'
import constants from '../../lib/constants'
export default {
    name: 'Logout',
    data(){
        return {
            constants
        }
    },
    methods: {
        logout() {
            this.$store.commit('SET_USERNAME',null)
            sessionStorage.removeItem('username')
            this.$router.back()
            // const request_header = {
            //         headers: {
            //         'Authorization': `Token ${this.$cookies.get('auth-token')}`
            //         }
            //     }

            // axios.post(constants.baseUrl+'rest-auth/logout/',null,request_header)
            // .then(()=>{
            //     this.constants.LS_KEY.USER_TOKEN = 'userToken'
            //     this.$cookies.remove('auth-token')
            //     this.$router.back()
            // })
        }
    },
    created() {
        this.logout()
    }


}
</script>

<style>

</style>