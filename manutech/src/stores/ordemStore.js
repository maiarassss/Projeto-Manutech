import { ref } from "vue";
import { defineStore } from "pinia";
import { api } from "@/services/api";

export const useOrdensStore = defineStore("ordens", () => {
  const ordens = ref([]);
  const loading = ref(false);
  const error = ref(null);

  function normalizarOrdem(ordem) {
    return {
      idOrdem: ordem.idOrdem,
      titulo: ordem.titulo || "",
      descricao: ordem.descricao || "",
      prioridade: ordem.prioridade || "BAIXA",
      status: ordem.status || "ABERTA",

      idMaquina:
        ordem.idMaquina ||
        ordem.maquina?.idMaquina ||
        ordem.maquinaResponseDTO?.idMaquina ||
        null,

      codigoIdentificador:
        ordem.codigoIdentificador ||
        ordem.maquina?.codigoIdentificador ||
        ordem.maquinaResponseDTO?.codigoIdentificador ||
        "",

      modelo:
        ordem.modelo ||
        ordem.maquina?.modelo ||
        ordem.maquinaResponseDTO?.modelo ||
        "",

      nomeSetor:
        ordem.nomeSetor ||
        ordem.maquina?.nomeSetor ||
        ordem.maquinaResponseDTO?.nomeSetor ||
        ordem.maquina?.setor?.nomeSetor ||
        ordem.maquinaResponseDTO?.setor?.nomeSetor ||
        "",

      idTecnico:
        ordem.idTecnico ||
        ordem.tecnico?.idTecnico ||
        ordem.tecnicoResponseDTO?.idTecnico ||
        null,

      nomeTecnico:
        ordem.nomeTecnico ||
        ordem.tecnico?.nome ||
        ordem.tecnicoResponseDTO?.nome ||
        "Não atribuído",
    };
  }

  async function buscarOrdens() {
    loading.value = true;
    error.value = null;

    try {
      const resposta = await api.get("/ordens");
      ordens.value = resposta.data.map(normalizarOrdem);
      return ordens.value;
    } catch (erro) {
      error.value = "Erro ao buscar ordens.";
      console.error(erro);
      return [];
    } finally {
      loading.value = false;
    }
  }

  function buscarOrdemPorId(id) {
    return ordens.value.find((ordem) => ordem.idOrdem === id);
  }

  async function adicionarOrdem(ordem) {
    loading.value = true;
    error.value = null;

    const ordemRequest = {
      titulo: ordem.titulo,
      descricao: ordem.descricao,
      prioridade: ordem.prioridade,
      status: ordem.status,
      idMaquina: ordem.idMaquina,
      idTecnico: ordem.idTecnico || null,
    };

    try {
      const resposta = await api.post("/ordens", ordemRequest);
      const novaOrdem = normalizarOrdem(resposta.data);

      ordens.value.push(novaOrdem);
      return novaOrdem;
    } catch (erro) {
      error.value = "Erro ao cadastrar ordem.";
      console.error(erro);
      throw erro;
    } finally {
      loading.value = false;
    }
  }

  async function atualizarOrdem(id, ordemAtualizada) {
    loading.value = true;
    error.value = null;

    const ordemRequest = {
      titulo: ordemAtualizada.titulo,
      descricao: ordemAtualizada.descricao,
      prioridade: ordemAtualizada.prioridade,
      status: ordemAtualizada.status,
      idMaquina: ordemAtualizada.idMaquina,
      idTecnico: ordemAtualizada.idTecnico || null,
    };

    try {
      const resposta = await api.put(`/ordens/${id}`, ordemRequest);
      const ordemSalva = normalizarOrdem(resposta.data);

      const index = ordens.value.findIndex((ordem) => ordem.idOrdem === id);

      if (index !== -1) {
        ordens.value[index] = ordemSalva;
      }

      return ordemSalva;
    } catch (erro) {
      error.value = "Erro ao atualizar ordem.";
      console.error(erro);
      throw erro;
    } finally {
      loading.value = false;
    }
  }

  async function cancelarOrdem(id) {
    const ordemAtual = ordens.value.find((ordem) => ordem.idOrdem === id);

    if (!ordemAtual) {
      error.value = "Ordem não encontrada.";
      return;
    }

    return atualizarOrdem(id, {
      titulo: ordemAtual.titulo,
      descricao: ordemAtual.descricao,
      prioridade: ordemAtual.prioridade,
      status: "CANCELADA",
      idMaquina: ordemAtual.idMaquina,
      idTecnico: ordemAtual.idTecnico,
    });
  }

  return {
    ordens,
    loading,
    error,
    buscarOrdens,
    buscarOrdemPorId,
    adicionarOrdem,
    atualizarOrdem,
    cancelarOrdem,
  };
});
