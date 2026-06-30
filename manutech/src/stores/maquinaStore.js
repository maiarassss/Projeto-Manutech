import { defineStore } from "pinia";
import { ref } from "vue";

export const useMaquinasStore = defineStore("maquinas", () => {
  const maquinas = ref([
    {
      idMaquina: 1,
      codigoIdentificador: "CORT-001",
      modelo: "Cortadeira Hidráulica",
      idSetor: 1,
      nomeSetor: "Corte",
      ativa: true,
    },
    {
      idMaquina: 2,
      codigoIdentificador: "COST-002",
      modelo: "Máquina de Costura Industrial",
      idSetor: 2,
      nomeSetor: "Costura",
      ativa: true,
    },
    {
      idMaquina: 3,
      codigoIdentificador: "MONT-003",
      modelo: "Máquina de Montagem de Cabedal",
      idSetor: 3,
      nomeSetor: "Montagem",
      ativa: false,
    },
    {
      idMaquina: 4,
      codigoIdentificador: "COLA-004",
      modelo: "Aplicadora de Cola",
      idSetor: 3,
      nomeSetor: "Montagem",
      ativa: true,
    },
    {
      idMaquina: 5,
      codigoIdentificador: "PREN-005",
      modelo: "Prensa Hidráulica",
      idSetor: 4,
      nomeSetor: "Acabamento",
      ativa: true,
    },
    {
      idMaquina: 6,
      codigoIdentificador: "LIXA-006",
      modelo: "Lixadeira de Couro",
      idSetor: 4,
      nomeSetor: "Acabamento",
      ativa: false,
    },
  ]);

  const loading = ref(false);
  const error = ref(null);

  async function buscarMaquinas() {
    return maquinas.value;
  }

  async function buscarMaquinaPorId(id) {
    return maquinas.value.find((maquina) => maquina.idMaquina == id);
  }

  async function adicionarMaquina(maquina) {
    const novaMaquina = {
      idMaquina: Date.now(),
      ...maquina,
    };

    maquinas.value.push(novaMaquina);

    return novaMaquina;
  }

  async function atualizarMaquina(id, maquina) {
    const index = maquinas.value.findIndex((m) => m.idMaquina == id);

    if (index !== -1) {
      maquinas.value[index] = {
        idMaquina: Number(id),
        ...maquina,
      };
    }

    return maquinas.value[index];
  }

  async function excluirMaquina(id) {
    maquinas.value = maquinas.value.filter(
      (maquina) => maquina.idMaquina != id,
    );
  }

  return {
    maquinas,
    loading,
    error,
    buscarMaquinas,
    buscarMaquinaPorId,
    adicionarMaquina,
    atualizarMaquina,
    excluirMaquina,
  };
});
