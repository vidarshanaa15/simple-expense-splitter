<template>
  <div class="flex justify-center items-center h-screen bg-gradient-to-br from-blue-100 to-blue-300">
    <div class="bg-white rounded-2xl shadow-lg p-8 w-full max-w-md">
      <h1 class="text-2xl font-bold text-center text-blue-700 mb-6">Login</h1>

      <form @submit.prevent="login">
        <div class="mb-4">
          <label class="block text-gray-700 mb-1">Username</label>
          <input v-model="username" type="text" class="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required />
        </div>

        <div class="mb-4">
          <label class="block text-gray-700 mb-1">Password</label>
          <input v-model="password" type="password" class="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required />
        </div>

        <p v-if="error" class="text-red-600 text-sm mb-4">{{ error }}</p>

        <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition-all">
          Login
        </button>
      </form>

      <p class="text-sm text-center text-gray-600 mt-4">
        New here?
        <router-link to="/register" class="text-blue-600 hover:underline font-medium">Register</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const error = ref('')

const login = () => {
  const storedUser = JSON.parse(localStorage.getItem('user'))
  if (!storedUser) {
    error.value = "No user found. Please register first."
    return
  }

  if (username.value === storedUser.username && password.value === storedUser.password) {
    alert('Login successful!')
    router.push('/')
  } else {
    error.value = "Invalid username or password"
  }
}
</script>
