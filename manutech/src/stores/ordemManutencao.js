import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useOrdensManutencaoStore = defineStore('ordens-manutencao', () => {
  const ordens = ref([
    { id: 1, titulo: 'Concertar manivéla' },
    { id: 2, titulo: 'Substituir peça danificada' },
    { id: 3, titulo: 'Limpar máquina' },
  ])

  function excluirOrdem(id) {
    ordens.value = ordens.value.filter(o => o.id !== id)
  }

  function adicionarOrdem(ordem) {
    ordens.value.push(ordem)
  }

  return {
    ordens,
    excluirOrdem,
    adicionarOrdem
  }
})