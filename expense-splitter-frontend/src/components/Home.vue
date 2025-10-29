<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <div class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <h1 class="text-3xl font-bold text-gray-900">Expense Splitter</h1>
        <p class="mt-1 text-sm text-gray-600">Manage your group expenses easily</p>
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

      <!-- Loading State -->
      <div v-if="loading" class="text-center py-12">
        <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        <p class="mt-4 text-gray-600">Loading groups...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-lg p-4">
        <p class="text-red-800">{{ error }}</p>
      </div>

      <!-- Empty State -->
      <div v-else-if="groups.length === 0" class="text-center py-12 bg-white rounded-lg shadow">
        <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
        </svg>
        <h3 class="mt-4 text-lg font-medium text-gray-900">No groups yet</h3>
        <p class="mt-2 text-gray-600">Create your first group to start splitting expenses!</p>
      </div>

      <!-- Groups Grid -->
      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="group in groups"
          :key="group.id"
          @click="goToGroup(group.id)"
          class="bg-white rounded-lg shadow hover:shadow-lg transition duration-200 cursor-pointer p-6 border border-gray-200 hover:border-blue-400"
        >
          <h3 class="text-xl font-semibold text-gray-900 mb-3">{{ group.name }}</h3>
          
          <div class="flex items-center gap-2 text-sm text-gray-600 mb-3">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
            </svg>
            <span>{{ group.members.length }} members</span>
          </div>

          <div class="flex flex-wrap gap-2">
            <span
              v-for="member in group.members"
              :key="member"
              class="inline-block bg-blue-100 text-blue-800 text-xs font-medium px-3 py-1 rounded-full"
            >
              {{ member }}
            </span>
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
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-2xl font-bold text-gray-900">Create New Group</h2>
          <button
            @click="closeModal"
            class="text-gray-400 hover:text-gray-600 transition"
          >
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form @submit.prevent="createGroup">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Group Name <span class="text-red-500">*</span>
            </label>
            <input
              v-model="newGroup.name"
              type="text"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              placeholder="e.g., College Trip"
            />
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Members <span class="text-red-500">*</span>
            </label>
            <input
              v-model="memberInput"
              type="text"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent mb-2"
              placeholder="Enter member name"
              @keyup.enter="addMember"
            />
            <button
              type="button"
              @click="addMember"
              class="text-sm text-blue-600 hover:text-blue-800 font-medium"
            >
              + Add Member
            </button>

            <div v-if="newGroup.members.length > 0" class="mt-3 flex flex-wrap gap-2">
              <span
                v-for="(member, index) in newGroup.members"
                :key="index"
                class="inline-flex items-center gap-1 bg-blue-100 text-blue-800 text-sm font-medium px-3 py-1 rounded-full"
              >
                {{ member }}
                <button
                  type="button"
                  @click="removeMember(index)"
                  class="text-blue-600 hover:text-blue-800"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </span>
            </div>
          </div>

          <div v-if="createError" class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
            <p class="text-sm text-red-800">{{ createError }}</p>
          </div>

          <div class="flex gap-3">
            <button
              type="button"
              @click="closeModal"
              class="flex-1 px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition"
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
export default {
  name: 'Home',
  data() {
    return {
      groups: [],
      loading: false,
      error: null,
      showCreateModal: false,
      creating: false,
      createError: null,
      newGroup: {
        name: '',
        members: []
      },
      memberInput: ''
    }
  },
  mounted() {
    this.fetchGroups()
  },
  methods: {
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
    
    goToGroup(groupId) {
      this.$router.push(`/groups/${groupId}`)
    },
    
    addMember() {
      const member = this.memberInput.trim()
      if (member && !this.newGroup.members.includes(member)) {
        this.newGroup.members.push(member)
        this.memberInput = ''
      }
    },
    
    removeMember(index) {
      this.newGroup.members.splice(index, 1)
    },
    
    async createGroup() {
      if (!this.newGroup.name.trim()) {
        this.createError = 'Please enter a group name'
        return
      }
      
      if (this.newGroup.members.length === 0) {
        this.createError = 'Please add at least one member'
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
    }
  }
}
</script>