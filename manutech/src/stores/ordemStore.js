import { ref } from "vue";
import { defineStore } from "pinia";

export const useOrdensStore = defineStore("ordens", () => {
  const ordens = ref([
    {
      idOrdem: 1,
      titulo: "Falha na cortadeira",
      descricao: "Máquina travando durante o processo de corte do couro.",
      prioridade: "ALTA",
      status: "ANDAMENTO",

      idMaquina: 1,
      codigoIdentificador: "CORT-001",
      modelo: "Cortadeira Hidráulica",
      nomeSetor: "Corte",

      idTecnico: 1,
      nomeTecnico: "João Silva",
    },
    {
      idOrdem: 2,
      titulo: "Revisão da máquina de costura",
      descricao: "Revisão preventiva na máquina de costura industrial.",
      prioridade: "MEDIA",
      status: "ANDAMENTO",

      idMaquina: 2,
      codigoIdentificador: "COST-002",
      modelo: "Máquina de Costura Industrial",
      nomeSetor: "Costura",

      idTecnico: 2,
      nomeTecnico: "Maria Oliveira",
    },
    {
      idOrdem: 3,
      titulo: "Lixadeira parada",
      descricao: "Equipamento não está ligando corretamente.",
      prioridade: "CRITICA",
      status: "CANCELADA",

      idMaquina: 6,
      codigoIdentificador: "LIXA-006",
      modelo: "Lixadeira de Couro",
      nomeSetor: "Acabamento",

      idTecnico: 3,
      nomeTecnico: "Pedro Santos",
    },
  ]);

  async function buscarOrdens() {
    return ordens.value;
  }

  async function buscarOrdemPorId(id) {
    return ordens.value.find((ordem) => ordem.idOrdem == id);
  }

  async function adicionarOrdem(ordem) {
    const novaOrdem = {
      idOrdem: Date.now(),
      ...ordem,
    };

    ordens.value.push(novaOrdem);

    return novaOrdem;
  }

  async function atualizarOrdem(id, ordem) {
    const index = ordens.value.findIndex((o) => o.idOrdem == id);

    if (index !== -1) {
      ordens.value[index] = {
        idOrdem: Number(id),
        ...ordem,
      };
    }

    return ordens.value[index];
  }

  async function cancelarOrdem(id) {
    const ordem = ordens.value.find((o) => o.idOrdem == id);

    if (ordem) {
      ordem.status = "CANCELADA";
    }

    return ordem;
  }

  return {
    ordens,
    buscarOrdens,
    buscarOrdemPorId,
    adicionarOrdem,
    atualizarOrdem,
    cancelarOrdem,
  };
});
