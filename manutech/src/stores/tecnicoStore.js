import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useTecnicosStore = defineStore('tecnicos', () => {
  const tecnicos = ref([
    { id: 1, nome: 'João Silva' },
    { id: 2, nome: 'Maria Oliveira' },
    { id: 3, nome: 'Pedro Santos' },
  ])

  function excluirTecnico(id) {
    tecnicos.value = tecnicos.value.filter(t => t.id !== id)
  }

  function adicionarTecnico(tecnico) {
    tecnicos.value.push(tecnico)
  }

  return {
    tecnicos,
    excluirTecnico,
    adicionarTecnico
  }
})