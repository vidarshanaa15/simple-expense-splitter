<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <div class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <button
          @click="goBack"
          class="mb-4 text-blue-600 hover:text-blue-800 flex items-center gap-2 transition"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
          Back to Groups
        </button>

        <div v-if="loadingGroup" class="animate-pulse">
          <div class="h-8 bg-gray-200 rounded w-1/3 mb-2"></div>
          <div class="h-4 bg-gray-200 rounded w-1/4"></div>
        </div>

        <div v-else-if="group">
          <h1 class="text-3xl font-bold text-gray-900">{{ group.name }}</h1>
          <div class="mt-3 flex flex-wrap gap-2">
            <span
              v-for="member in group.members"
              :key="member"
              class="inline-block bg-blue-100 text-blue-800 text-sm font-medium px-3 py-1 rounded-full"
            >
              {{ member }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Error State -->
      <div v-if="error" class="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
        <p class="text-red-800">{{ error }}</p>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Left Column: Expenses -->
        <div class="lg:col-span-2 space-y-6">
          <!-- Add Expense Button -->
          <button
            @click="showAddExpenseModal = true"
            class="w-full bg-green-600 hover:bg-green-700 text-white font-semibold py-3 px-6 rounded-lg shadow-md transition duration-200 flex items-center justify-center gap-2"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            Add Expense
          </button>

          <!-- Expenses List -->
          <div class="bg-white rounded-lg shadow">
            <div class="px-6 py-4 border-b border-gray-200">
              <h2 class="text-xl font-semibold text-gray-900">Expenses</h2>
            </div>

            <div v-if="loadingExpenses" class="p-6">
              <div class="animate-pulse space-y-4">
                <div class="h-16 bg-gray-200 rounded"></div>
                <div class="h-16 bg-gray-200 rounded"></div>
                <div class="h-16 bg-gray-200 rounded"></div>
              </div>
            </div>

            <div v-else-if="expenses.length === 0" class="p-6 text-center text-gray-600">
              <svg class="mx-auto h-12 w-12 text-gray-400 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              <p>No expenses yet. Add your first expense!</p>
            </div>

            <div v-else class="divide-y divide-gray-200">
              <div
                v-for="expense in expenses"
                :key="expense.id"
                class="p-4 hover:bg-gray-50 transition"
              >
                <div class="flex justify-between items-start">
                  <div class="flex-1">
                    <h3 class="font-semibold text-gray-900">{{ expense.title }}</h3>
                    <p class="text-sm text-gray-600 mt-1">
                      Paid by <span class="font-medium">{{ expense.paidBy }}</span>
                    </p>
                    <p class="text-xs text-gray-500 mt-1">{{ formatDate(expense.date) }}</p>
                  </div>
                  <div class="flex items-center gap-3">
                    <span class="text-lg font-bold text-gray-900">₹{{ expense.amount.toFixed(2) }}</span>
                    <div class="flex gap-2">
                      <button
                        @click="openEditModal(expense)"
                        class="text-blue-600 hover:text-blue-800 transition"
                        title="Edit"
                      >
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                        </svg>
                      </button>
                      <button
                        @click="confirmDelete(expense.id)"
                        class="text-red-600 hover:text-red-800 transition"
                        title="Delete"
                      >
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Right Column: Summary -->
        <div class="lg:col-span-1">
          <div class="bg-white rounded-lg shadow sticky top-4">
            <div class="px-6 py-4 border-b border-gray-200">
              <h2 class="text-xl font-semibold text-gray-900">Balance Summary</h2>
            </div>

            <div v-if="loadingSummary" class="p-6">
              <div class="animate-pulse space-y-3">
                <div class="h-12 bg-gray-200 rounded"></div>
                <div class="h-12 bg-gray-200 rounded"></div>
              </div>
            </div>

            <div v-else-if="Object.keys(summary).length === 0" class="p-6 text-center text-gray-600">
              <p class="text-sm">No balances to display</p>
            </div>

            <div v-else class="p-6 space-y-3">
              <div
                v-for="(balance, member) in summary"
                :key="member"
                class="p-4 rounded-lg"
                :class="balance > 0 ? 'bg-green-50' : balance < 0 ? 'bg-red-50' : 'bg-gray-50'"
              >
                <p class="text-sm font-semibold" :class="balance > 0 ? 'text-green-800' : balance < 0 ? 'text-red-800' : 'text-gray-800'">
                  {{ member }}
                </p>
                <p class="text-xl font-bold mt-1" :class="balance > 0 ? 'text-green-600' : balance < 0 ? 'text-red-600' : 'text-gray-600'">
                  <span v-if="balance > 0">+₹{{ balance.toFixed(2)}} (gets back)</span>
                  <span v-else-if="balance < 0">-₹{{ Math.abs(balance).toFixed(2) }} (owes)</span>
                  <span v-else>₹0.00 (settled)</span>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Add Expense Modal -->
    <div
      v-if="showAddExpenseModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
      @click.self="closeAddExpenseModal"
    >
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-2xl font-bold text-gray-900">Add Expense</h2>
          <button @click="closeAddExpenseModal" class="text-gray-400 hover:text-gray-600 transition">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form @submit.prevent="addExpense">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Title <span class="text-red-500">*</span>
            </label>
            <input
              v-model="newExpense.title"
              type="text"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
              placeholder="e.g., Lunch"
            />
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Amount (₹) <span class="text-red-500">*</span>
            </label>
            <input
              v-model.number="newExpense.amount"
              type="number"
              step="0.01"
              required
              min="0.01"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
              placeholder="0.00"
            />
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Paid By <span class="text-red-500">*</span>
            </label>
            <select
              v-model="newExpense.paidBy"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
            >
              <option value="">Select member</option>
              <option v-for="member in group?.members" :key="member" :value="member">
                {{ member }}
              </option>
            </select>
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Date <span class="text-red-500">*</span>
            </label>
            <input
              v-model="newExpense.date"
              type="date"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
            />
          </div>

          <div v-if="expenseError" class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
            <p class="text-sm text-red-800">{{ expenseError }}</p>
          </div>

          <div class="flex gap-3">
            <button
              type="button"
              @click="closeAddExpenseModal"
              class="flex-1 px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="addingExpense"
              class="flex-1 px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg font-semibold transition disabled:opacity-50"
            >
              {{ addingExpense ? 'Adding...' : 'Add Expense' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Edit Expense Modal -->
    <div
      v-if="showEditExpenseModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
      @click.self="closeEditExpenseModal"
    >
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-2xl font-bold text-gray-900">Edit Expense</h2>
          <button @click="closeEditExpenseModal" class="text-gray-400 hover:text-gray-600 transition">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form @submit.prevent="updateExpense">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Title <span class="text-red-500">*</span>
            </label>
            <input
              v-model="editingExpense.title"
              type="text"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Amount (₹) <span class="text-red-500">*</span>
            </label>
            <input
              v-model.number="editingExpense.amount"
              type="number"
              step="0.01"
              required
              min="0.01"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Paid By <span class="text-red-500">*</span>
            </label>
            <select
              v-model="editingExpense.paidBy"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option v-for="member in group?.members" :key="member" :value="member">
                {{ member }}
              </option>
            </select>
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Date <span class="text-red-500">*</span>
            </label>
            <input
              v-model="editingExpense.date"
              type="date"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <div v-if="expenseError" class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
            <p class="text-sm text-red-800">{{ expenseError }}</p>
          </div>

          <div class="flex gap-3">
            <button
              type="button"
              @click="closeEditExpenseModal"
              class="flex-1 px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="updatingExpense"
              class="flex-1 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-semibold transition disabled:opacity-50"
            >
              {{ updatingExpense ? 'Updating...' : 'Update Expense' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div
      v-if="showDeleteModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
      @click.self="showDeleteModal = false"
    >
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h2 class="text-2xl font-bold text-gray-900 mb-4">Delete Expense?</h2>
        <p class="text-gray-600 mb-6">Are you sure you want to delete this expense? This action cannot be undone.</p>
        
        <div class="flex gap-3">
          <button
            @click="showDeleteModal = false"
            class="flex-1 px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition"
          >
            Cancel
          </button>
          <button
            @click="deleteExpense"
            :disabled="deletingExpense"
            class="flex-1 px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg font-semibold transition disabled:opacity-50"
          >
            {{ deletingExpense ? 'Deleting...' : 'Delete' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GroupDetail',
  data() {
    return {
      group: null,
      expenses: [],
      summary: {},
      loadingGroup: false,
      loadingExpenses: false,
      loadingSummary: false,
      error: null,
      showAddExpenseModal: false,
      showEditExpenseModal: false,
      showDeleteModal: false,
      addingExpense: false,
      updatingExpense: false,
      deletingExpense: false,
      expenseError: null,
      expenseToDelete: null,
      newExpense: {
        title: '',
        amount: '',
        paidBy: '',
        date: new Date().toISOString().split('T')[0]
      },
      editingExpense: {
        id: null,
        title: '',
        amount: '',
        paidBy: '',
        date: ''
      }
    }
  },
  mounted() {
    this.fetchAllData()
  },
  methods: {
    async fetchAllData() {
      const groupId = this.$route.params.id
      await this.fetchGroup(groupId)
      await this.fetchExpenses(groupId)
      await this.fetchSummary(groupId)
    },
    
    async fetchGroup(groupId) {
      this.loadingGroup = true
      this.error = null
      
      try {
        const response = await fetch(`http://localhost:8080/api/groups/${groupId}`)
        if (!response.ok) throw new Error('Failed to fetch group details')
        this.group = await response.json()
      } catch (err) {
        this.error = err.message
        console.error('Error fetching group:', err)
      } finally {
        this.loadingGroup = false
      }
    },
    
    async fetchExpenses(groupId) {
      this.loadingExpenses = true
      
      try {
        const response = await fetch(`http://localhost:8080/api/groups/${groupId}/expenses`)
        if (!response.ok) throw new Error('Failed to fetch expenses')
        this.expenses = await response.json()
      } catch (err) {
        console.error('Error fetching expenses:', err)
        this.expenses = []
      } finally {
        this.loadingExpenses = false
      }
    },
    
    async fetchSummary(groupId) {
      this.loadingSummary = true
      
      try {
        const response = await fetch(`http://localhost:8080/api/groups/${groupId}/summary`)
        if (!response.ok) throw new Error('Failed to fetch summary')
        this.summary = await response.json()
      } catch (err) {
        console.error('Error fetching summary:', err)
        this.summary = {}
      } finally {
        this.loadingSummary = false
      }
    },
    
    async addExpense() {
      this.addingExpense = true
      this.expenseError = null
      
      try {
        const response = await fetch(`http://localhost:8080/api/groups/${this.$route.params.id}/expenses`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(this.newExpense)
        })
        
        if (!response.ok) {
          const errorData = await response.text()
          throw new Error(errorData || 'Failed to add expense')
        }
        
        await this.fetchExpenses(this.$route.params.id)
        await this.fetchSummary(this.$route.params.id)
        this.closeAddExpenseModal()
      } catch (err) {
        this.expenseError = err.message
        console.error('Error adding expense:', err)
      } finally {
        this.addingExpense = false
      }
    },
    
    openEditModal(expense) {
      this.editingExpense = {
        id: expense.id,
        title: expense.title,
        amount: expense.amount,
        paidBy: expense.paidBy,
        date: expense.date
      }
      this.showEditExpenseModal = true
    },
    
    async updateExpense() {
      this.updatingExpense = true
      this.expenseError = null
      
      try {
        const response = await fetch(`http://localhost:8080/api/expenses/${this.editingExpense.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            title: this.editingExpense.title,
            amount: this.editingExpense.amount,
            paidBy: this.editingExpense.paidBy,
            date: this.editingExpense.date
          })
        })
        
        if (!response.ok) throw new Error('Failed to update expense')
        
        await this.fetchExpenses(this.$route.params.id)
        await this.fetchSummary(this.$route.params.id)
        this.closeEditExpenseModal()
      } catch (err) {
        this.expenseError = err.message
        console.error('Error updating expense:', err)
      } finally {
        this.updatingExpense = false
      }
    },
    
    confirmDelete(expenseId) {
      this.expenseToDelete = expenseId
      this.showDeleteModal = true
    },
    
    async deleteExpense() {
      this.deletingExpense = true
      
      try {
        const response = await fetch(`http://localhost:8080/api/expenses/${this.expenseToDelete}`, {
          method: 'DELETE'
        })
        
        if (!response.ok) throw new Error('Failed to delete expense')
        
        await this.fetchExpenses(this.$route.params.id)
        await this.fetchSummary(this.$route.params.id)
        this.showDeleteModal = false
        this.expenseToDelete = null
      } catch (err) {
        this.error = err.message
        console.error('Error deleting expense:', err)
      } finally {
        this.deletingExpense = false
      }
    },
    
    closeAddExpenseModal() {
      this.showAddExpenseModal = false
      this.newExpense = {
        title: '',
        amount: '',
        paidBy: '',
        date: new Date().toISOString().split('T')[0]
      }
      this.expenseError = null
    },
    
    closeEditExpenseModal() {
      this.showEditExpenseModal = false
      this.editingExpense = { id: null, title: '', amount: '', paidBy: '', date: '' }
      this.expenseError = null
    },
    
    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', { 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric' 
      })
    },
    
    goBack() {
      this.$router.push('/')
    }
  }
}
</script>