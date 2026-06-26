import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useMaquinasStore = defineStore('maquinas', () => {
  const maquinas = ref([
    { id: 1, nome: 'Máquina A' },
    { id: 2, nome: 'Máquina B' },
    { id: 3, nome: 'Máquina C' },
  ])

  function excluirMaquina(id) {
    maquinas.value = maquinas.value.filter(m => m.id !== id)
  }

  function adicionarMaquina(maquina) {
    maquinas.value.push(maquina)
  }

  return {
    maquinas,
    excluirMaquina,
    adicionarMaquina
  }
})