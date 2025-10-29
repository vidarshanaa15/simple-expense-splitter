<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-200">
    <!-- Header -->
    <div class="bg-white dark:bg-gray-800 shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div class="flex justify-between items-center">
          <div>
            <h1 class="text-3xl font-bold text-gray-900 dark:text-white">Expense Splitter</h1>
            <p class="mt-1 text-sm text-gray-600 dark:text-gray-400">Manage your group expenses easily</p>
          </div>
          
          <!-- Dark Mode Toggle -->
          <button
            @click="toggleDarkMode"
            class="p-2 rounded-lg bg-gray-200 dark:bg-gray-700 hover:bg-gray-300 dark:hover:bg-gray-600 transition"
            title="Toggle Dark Mode"
          >
            <svg v-if="!isDark" class="w-6 h-6 text-gray-800" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
            </svg>
            <svg v-else class="w-6 h-6 text-yellow-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
            </svg>
          </button>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Create Group Button -->
      <div class="mb-6">
        <button
          @click="showCreateModal = true"
          class="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg shadow-md transition duration-200 flex items-center gap-2"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          Create New Group
        </button>
      </div>

      <!-- Recent Groups Section -->
      <div v-if="recentGroups.length > 0" class="mb-8">
        <h2 class="text-xl font-semibold text-gray-900 dark:text-white mb-4 flex items-center gap-2">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          Recently Viewed
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div
            v-for="group in recentGroups"
            :key="group.id"
            @click="goToGroup(group.id)"
            class="bg-gradient-to-br from-blue-50 to-indigo-50 dark:from-gray-700 dark:to-gray-600 rounded-lg shadow hover:shadow-lg transition duration-200 cursor-pointer p-4 border-2 border-blue-200 dark:border-blue-500"
          >
            <div class="flex items-center gap-2 mb-2">
              <svg class="w-4 h-4 text-blue-600 dark:text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
              <h3 class="text-lg font-semibold text-gray-900 dark:text-white">{{ group.name }}</h3>
            </div>
            <div class="flex flex-wrap gap-1">
              <span
                v-for="member in group.members.slice(0, 3)"
                :key="member"
                class="inline-block bg-blue-200 dark:bg-blue-800 text-blue-900 dark:text-blue-100 text-xs font-medium px-2 py-1 rounded-full"
              >
                {{ member }}
              </span>
              <span
                v-if="group.members.length > 3"
                class="inline-block bg-blue-200 dark:bg-blue-800 text-blue-900 dark:text-blue-100 text-xs font-medium px-2 py-1 rounded-full"
              >
                +{{ group.members.length - 3 }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- All Groups Section -->
      <div>
        <h2 class="text-xl font-semibold text-gray-900 dark:text-white mb-4">All Groups</h2>

        <!-- Loading State -->
        <div v-if="loading" class="text-center py-12">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
          <p class="mt-4 text-gray-600 dark:text-gray-400">Loading groups...</p>
        </div>

        <!-- Error State -->
        <div v-else-if="error" class="bg-red-50 dark:bg-red-900 border border-red-200 dark:border-red-700 rounded-lg p-4">
          <p class="text-red-800 dark:text-red-200">{{ error }}</p>
        </div>

        <!-- Empty State -->
        <div v-else-if="groups.length === 0" class="text-center py-12 bg-white dark:bg-gray-800 rounded-lg shadow">
          <svg class="mx-auto h-12 w-12 text-gray-400 dark:text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
          <h3 class="mt-4 text-lg font-medium text-gray-900 dark:text-white">No groups yet</h3>
          <p class="mt-2 text-gray-600 dark:text-gray-400">Create your first group to start splitting expenses!</p>
        </div>

        <!-- Groups Grid -->
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="group in groups"
            :key="group.id"
            @click="goToGroup(group.id)"
            class="bg-white dark:bg-gray-800 rounded-lg shadow hover:shadow-lg transition duration-200 cursor-pointer p-6 border border-gray-200 dark:border-gray-700 hover:border-blue-400 dark:hover:border-blue-500"
          >
            <h3 class="text-xl font-semibold text-gray-900 dark:text-white mb-3">{{ group.name }}</h3>
            
            <div class="flex items-center gap-2 text-sm text-gray-600 dark:text-gray-400 mb-3">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
              </svg>
              <span>{{ group.members.length }} members</span>
            </div>

            <div class="flex flex-wrap gap-2">
              <span
                v-for="member in group.members"
                :key="member"
                class="inline-block bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 text-xs font-medium px-3 py-1 rounded-full"
              >
                {{ member }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Create Group Modal -->
    <div
      v-if="showCreateModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
      @click.self="closeModal"
    >
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-md w-full p-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-2xl font-bold text-gray-900 dark:text-white">Create New Group</h2>
          <button
            @click="closeModal"
            class="text-gray-400 hover:text-gray-600 dark:hover:text-gray-300 transition"
          >
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form @submit.prevent="createGroup">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              Group Name <span class="text-red-500">*</span>
            </label>
            <input
              v-model="newGroup.name"
              @input="validateGroupName"
              @blur="validateGroupName"
              type="text"
              required
              class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
              :class="validationErrors.groupName ? 'border-red-500 dark:border-red-500 bg-red-50 dark:bg-red-900/20' : 'border-gray-300 dark:border-gray-600 dark:bg-gray-700 dark:text-white'"
              placeholder="e.g., College Trip"
            />
            <p v-if="validationErrors.groupName" class="mt-1 text-sm text-red-600 dark:text-red-400">
              {{ validationErrors.groupName }}
            </p>
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              Members <span class="text-red-500">*</span>
            </label>
            <input
              v-model="memberInput"
              @input="validateMemberInput"
              @blur="validateMemberInput"
              type="text"
              class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent mb-2 transition"
              :class="validationErrors.memberInput ? 'border-red-500 dark:border-red-500 bg-red-50 dark:bg-red-900/20' : 'border-gray-300 dark:border-gray-600 dark:bg-gray-700 dark:text-white'"
              placeholder="Enter member name"
              @keyup.enter="addMember"
            />
            <p v-if="validationErrors.memberInput" class="mb-2 text-sm text-red-600 dark:text-red-400">
              {{ validationErrors.memberInput }}
            </p>
            <button
              type="button"
              @click="addMember"
              class="text-sm text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300 font-medium"
            >
              + Add Member
            </button>

            <div v-if="newGroup.members.length > 0" class="mt-3 flex flex-wrap gap-2">
              <span
                v-for="(member, index) in newGroup.members"
                :key="index"
                class="inline-flex items-center gap-1 bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 text-sm font-medium px-3 py-1 rounded-full"
              >
                {{ member }}
                <button
                  type="button"
                  @click="removeMember(index)"
                  class="text-blue-600 dark:text-blue-300 hover:text-blue-800 dark:hover:text-blue-100"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </span>
            </div>
            <p v-if="validationErrors.members" class="mt-2 text-sm text-amber-600 dark:text-amber-400">
              {{ validationErrors.members }}
            </p>
          </div>

          <div v-if="createError" class="mb-4 p-3 bg-red-50 dark:bg-red-900 border border-red-200 dark:border-red-700 rounded-lg">
            <p class="text-sm text-red-800 dark:text-red-200">{{ createError }}</p>
          </div>

          <div class="flex gap-3">
            <button
              type="button"
              @click="closeModal"
              class="flex-1 px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 transition"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="creating"
              class="flex-1 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-semibold transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {{ creating ? 'Creating...' : 'Create Group' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { useDarkMode } from '../composables/useDarkMode'

export default {
  name: 'Home',
  setup() {
    const { isDark, toggleDarkMode, initTheme } = useDarkMode()
    return { isDark, toggleDarkMode, initTheme }
  },
  data() {
    return {
      groups: [],
      recentGroups: [],
      loading: false,
      error: null,
      showCreateModal: false,
      creating: false,
      createError: null,
      newGroup: {
        name: '',
        members: []
      },
      memberInput: '',
      validationErrors: {
        groupName: '',
        members: '',
        memberInput: ''
      }
    }
  },
  mounted() {
    this.initTheme()
    this.fetchGroups()
    this.loadRecentGroups()
  },
  methods: {
    validateGroupName() {
      if (!this.newGroup.name.trim()) {
        this.validationErrors.groupName = 'Group name is required'
      } else if (this.newGroup.name.trim().length < 3) {
        this.validationErrors.groupName = 'Group name must be at least 3 characters'
      } else if (this.newGroup.name.trim().length > 50) {
        this.validationErrors.groupName = 'Group name must be less than 50 characters'
      } else {
        this.validationErrors.groupName = ''
      }
    },

    validateMembers() {
      if (this.newGroup.members.length === 0) {
        this.validationErrors.members = 'At least one member is required'
      } else if (this.newGroup.members.length < 2) {
        this.validationErrors.members = 'At least 2 members are recommended for group expenses'
      } else {
        this.validationErrors.members = ''
      }
    },

    validateMemberInput() {
      const member = this.memberInput.trim()
      if (member && member.length < 2) {
        this.validationErrors.memberInput = 'Member name must be at least 2 characters'
      } else if (member && member.length > 30) {
        this.validationErrors.memberInput = 'Member name must be less than 30 characters'
      } else if (member && this.newGroup.members.includes(member)) {
        this.validationErrors.memberInput = 'This member already exists'
      } else {
        this.validationErrors.memberInput = ''
      }
    },

    async fetchGroups() {
      this.loading = true
      this.error = null
      
      try {
        const response = await fetch('http://localhost:8080/api/groups')
        
        if (!response.ok) {
          throw new Error('Failed to fetch groups')
        }
        
        this.groups = await response.json()
      } catch (err) {
        this.error = err.message
        console.error('Error fetching groups:', err)
      } finally {
        this.loading = false
      }
    },
    
    loadRecentGroups() {
      const recentIds = JSON.parse(localStorage.getItem('recentGroups') || '[]')
      
      // Filter groups that exist in recent IDs (max 3)
      this.recentGroups = this.groups
        .filter(group => recentIds.includes(group.id))
        .slice(0, 3)
        .sort((a, b) => recentIds.indexOf(a.id) - recentIds.indexOf(b.id))
    },
    
    goToGroup(groupId) {
      // Save to recent groups
      let recentIds = JSON.parse(localStorage.getItem('recentGroups') || '[]')
      
      // Remove if already exists
      recentIds = recentIds.filter(id => id !== groupId)
      
      // Add to front
      recentIds.unshift(groupId)
      
      // Keep only last 5
      recentIds = recentIds.slice(0, 5)
      
      localStorage.setItem('recentGroups', JSON.stringify(recentIds))
      
      this.$router.push(`/groups/${groupId}`)
    },
    
    addMember() {
      this.validateMemberInput()
      
      if (this.validationErrors.memberInput) {
        return
      }
      
      const member = this.memberInput.trim()
      if (member && !this.newGroup.members.includes(member)) {
        this.newGroup.members.push(member)
        this.memberInput = ''
        this.validationErrors.memberInput = ''
        this.validateMembers()
      }
    },
    
    removeMember(index) {
      this.newGroup.members.splice(index, 1)
      this.validateMembers()
    },
    
    async createGroup() {
      // Validate all fields
      this.validateGroupName()
      this.validateMembers()
      
      // Check if there are any validation errors
      if (this.validationErrors.groupName || this.validationErrors.members) {
        return
      }
      
      this.creating = true
      this.createError = null
      
      try {
        const response = await fetch('http://localhost:8080/api/groups', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            name: this.newGroup.name,
            members: this.newGroup.members
          })
        })
        
        if (!response.ok) {
          throw new Error('Failed to create group')
        }
        
        const createdGroup = await response.json()
        
        // Close modal and reset form
        this.closeModal()
        
        // Redirect to the newly created group
        this.$router.push(`/groups/${createdGroup.id}`)
      } catch (err) {
        this.createError = err.message
        console.error('Error creating group:', err)
      } finally {
        this.creating = false
      }
    },
    
    closeModal() {
      this.showCreateModal = false
      this.newGroup = { name: '', members: [] }
      this.memberInput = ''
      this.createError = null
      this.validationErrors = {
        groupName: '',
        members: '',
        memberInput: ''
      }
    }
  },
  watch: {
    groups() {
      // Reload recent groups when groups data changes
      this.loadRecentGroups()
    }
  }
}
</script>code src/pages/GroupPage.vue