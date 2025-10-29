import { ref, watch } from 'vue'

export function useDarkMode() {
    const isDark = ref(false)

    const applyTheme = () => {
        if (isDark.value) {
            document.documentElement.classList.add('dark')
        } else {
            document.documentElement.classList.remove('dark')
        }
    }

    const toggleDarkMode = () => {
        isDark.value = !isDark.value
        localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
        applyTheme()
    }

    const initTheme = () => {
        const savedTheme = localStorage.getItem('theme')
        isDark.value = savedTheme === 'dark'
        applyTheme()
    }

    // Watch for changes and apply theme
    watch(isDark, () => {
        applyTheme()
    })

    return {
        isDark,
        toggleDarkMode,
        initTheme
    }
}