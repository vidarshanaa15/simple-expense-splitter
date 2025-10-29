<template>
  <div class="flex justify-center items-center h-screen bg-gradient-to-br from-purple-500 to-indigo-600">
    <div class="bg-white rounded-2xl shadow-lg p-8 w-full max-w-md">
      <h1 class="text-2xl font-bold text-center text-purple-700 mb-6">Create an Account</h1>

      <form @submit.prevent="register">
        <div class="mb-4">
          <label class="block text-gray-700 mb-1">Username</label>
          <input v-model="form.username" type="text" class="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500" required />
        </div>

        <div class="mb-4">
          <label class="block text-gray-700 mb-1">Full Name</label>
          <input v-model="form.name" type="text" class="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500" required />
        </div>

        <div class="mb-4">
          <label class="block text-gray-700 mb-1">Email</label>
          <input v-model="form.email" type="email" class="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500" required />
        </div>

        <div class="mb-4">
          <label class="block text-gray-700 mb-1">Password</label>
          <input v-model="form.password" type="password" class="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500" required />
        </div>

        <div class="mb-4">
          <label class="block text-gray-700 mb-1">Confirm Password</label>
          <input v-model="form.confirmPassword" type="password" class="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500" required />
        </div>

        <p v-if="error" class="text-red-600 text-sm mb-4">{{ error }}</p>

        <button type="submit" class="w-full bg-purple-600 text-white py-2 rounded-lg hover:bg-purple-700 transition-all">
          Register
        </button>
      </form>

      <p class="text-sm text-center text-gray-600 mt-4">
        Already registered?
        <router-link to="/login" class="text-purple-600 hover:underline font-medium">Login</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = ref({
  username: '',
  name: '',
  email: '',
  password: '',
  confirmPassword: ''
})
const error = ref('')

const register = () => {
  if (form.value.password !== form.value.confirmPassword) {
    error.value = "Passwords don't match. Please try again"
    return
  }

  localStorage.setItem('user', JSON.stringify({
    username: form.value.username,
    password: form.value.password
  }))

  alert('Registration successful!')
  router.push('/login')
}
</script>
