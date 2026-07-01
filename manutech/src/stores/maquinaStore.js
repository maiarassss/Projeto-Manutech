import { ref } from "vue";
import { defineStore } from "pinia";
import { api } from "@/services/api";

export const useMaquinasStore = defineStore("maquinas", () => {
  const maquinas = ref([]);
  const loading = ref(false);
  const error = ref(null);

  function normalizarMaquina(maquina) {
    return {
      idMaquina: maquina.idMaquina,
      codigoIdentificador: maquina.codigoIdentificador,
      modelo: maquina.modelo,
      ativa: maquina.ativa,
      idSetor:
        maquina.idSetor ||
        maquina.setor?.idSetor ||
        maquina.setorResponseDTO?.idSetor ||
        null,
      nomeSetor:
        maquina.nomeSetor ||
        maquina.setor?.nomeSetor ||
        maquina.setorResponseDTO?.nomeSetor ||
        "Sem setor",
    };
  }

  async function buscarMaquinas() {
    loading.value = true;
    error.value = null;

    try {
      const resposta = await api.get("/maquinas");
      maquinas.value = resposta.data.map(normalizarMaquina);
      return maquinas.value;
    } catch (erro) {
      error.value = "Erro ao buscar máquinas.";
      console.error(erro);
      return [];
    } finally {
      loading.value = false;
    }
  }

  function buscarMaquinaPorId(id) {
    return maquinas.value.find((maquina) => maquina.idMaquina === id);
  }

  async function adicionarMaquina(maquina) {
    loading.value = true;
    error.value = null;

    const maquinaRequest = {
      codigoIdentificador: maquina.codigoIdentificador,
      modelo: maquina.modelo,
      ativa: maquina.ativa,
      idSetor: maquina.idSetor,
    };

    try {
      const resposta = await api.post("/maquinas", maquinaRequest);
      const novaMaquina = normalizarMaquina(resposta.data);

      maquinas.value.push(novaMaquina);

      return novaMaquina;
    } catch (erro) {
      error.value = "Erro ao cadastrar máquina.";
      console.error(erro);
      throw erro;
    } finally {
      loading.value = false;
    }
  }

  async function atualizarMaquina(id, maquinaAtualizada) {
    loading.value = true;
    error.value = null;

    const maquinaRequest = {
      codigoIdentificador: maquinaAtualizada.codigoIdentificador,
      modelo: maquinaAtualizada.modelo,
      ativa: maquinaAtualizada.ativa,
      idSetor: maquinaAtualizada.idSetor,
    };

    try {
      const resposta = await api.put(`/maquinas/${id}`, maquinaRequest);
      const maquinaSalva = normalizarMaquina(resposta.data);

      const index = maquinas.value.findIndex(
        (maquina) => maquina.idMaquina === id,
      );

      if (index !== -1) {
        maquinas.value[index] = maquinaSalva;
      }

      return maquinaSalva;
    } catch (erro) {
      error.value = "Erro ao atualizar máquina.";
      console.error(erro);
      throw erro;
    } finally {
      loading.value = false;
    }
  }

  return {
    maquinas,
    loading,
    error,
    buscarMaquinas,
    buscarMaquinaPorId,
    adicionarMaquina,
    atualizarMaquina,
  };
});
