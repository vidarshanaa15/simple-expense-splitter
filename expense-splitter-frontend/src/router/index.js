import { createRouter, createWebHistory } from 'vue-router'
import Home from '../components/Home.vue'
import GroupPage from '../components/GroupPage.vue'
import Register from '../components/Register.vue'
import Login from '../components/Login.vue'

const routes = [
    { path: '/', component: Home },
    { path: '/groups/:id', component: GroupPage },
    { path: '/register', component: Register },
    { path: '/login', component: Login },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
