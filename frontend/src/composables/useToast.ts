import { reactive } from 'vue'

export interface ToastItem {
  id: number
  type: 'success' | 'error' | 'info'
  text: string
}

const state = reactive<{ items: ToastItem[] }>({ items: [] })
let seq = 1

export function useToast() {
  function push(type: ToastItem['type'], text: string) {
    const id = seq++
    state.items.push({ id, type, text })
    setTimeout(() => remove(id), 2600)
  }
  function remove(id: number) {
    const i = state.items.findIndex((t) => t.id === id)
    if (i >= 0) state.items.splice(i, 1)
  }
  return {
    state,
    success: (t: string) => push('success', t),
    error: (t: string) => push('error', t),
    info: (t: string) => push('info', t),
    remove
  }
}
