<template>
    <div id="header" v-if="isHeader">
        <h1>
            <router-link v-bind:to="{name:constants.URL_TYPE.POST.MAIN}">
                {{headerTitle}}
            </router-link>
        </h1>
        <div class="right">
            <div class="search-input">
                <i class="fas fa-search"></i>
                <input v-model="keyword" type="text"/>
            </div> 
            
            <router-link v-if="constants.LS_KEY.USER_TOKEN == 'userToken'" v-bind:to="{name:constants.URL_TYPE.USER.LOGIN}" class="login-btn">
                로그인 
            </router-link>  
            <router-link v-else v-bind:to="{name:constants.URL_TYPE.USER.LOGOUT}" class="login-btn">
                로그아웃
            </router-link>
            <router-link to="/create"  class="login-btn">새글쓰기</router-link>
        </div>  
        
    </div>
</template>   

<script> 
    import constants from '../../lib/constants'
    import { mapState } from 'vuex'
    export default {
        name: 'Header',
        components: { 

        },
        props: ['isHeader'],
        computed:{
            ...mapState([
                'headerTitle'
            ])
        },
        watch: {
        },
        created() {
        },
        methods : {
        },
        data: function() {
           return {
               constants,
               keyword : "",
           }
        },

    }
</script>
