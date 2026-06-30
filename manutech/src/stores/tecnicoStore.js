import { ref } from "vue";
import { defineStore } from "pinia";
import { api } from "@/services/api";

export const useTecnicosStore = defineStore("tecnicos", () => {
  const tecnicos = ref([]);
  const loading = ref(false);
  const error = ref(null);

  function normalizarTecnico(tecnico) {
    const setores =
      tecnico.setoresAtendidos ||
      tecnico.setores ||
      tecnico.setoresResponseDTO ||
      tecnico.setorResponseDTO ||
      [];

    return {
      idTecnico: tecnico.idTecnico,
      nome: tecnico.nome,
      telefone: tecnico.telefone,
      setoresAtendidos: Array.isArray(setores)
        ? setores.map((setor) => ({
            idSetor: setor.idSetor,
            nomeSetor: setor.nomeSetor,
          }))
        : [],
    };
  }

  async function buscarTecnicos() {
    loading.value = true;
    error.value = null;

    try {
      const resposta = await api.get("/tecnicos");
      tecnicos.value = resposta.data.map(normalizarTecnico);
      return tecnicos.value;
    } catch (erro) {
      error.value = "Erro ao buscar técnicos.";
      console.error(erro);
      return [];
    } finally {
      loading.value = false;
    }
  }

  function buscarTecnicoPorId(id) {
    return tecnicos.value.find((tecnico) => tecnico.idTecnico === id);
  }

  async function adicionarTecnico(tecnico) {
    loading.value = true;
    error.value = null;

    const tecnicoRequest = {
      nome: tecnico.nome,
      telefone: tecnico.telefone,
      idsSetores: tecnico.idsSetores,
    };

    try {
      const resposta = await api.post("/tecnicos", tecnicoRequest);
      const novoTecnico = normalizarTecnico(resposta.data);

      tecnicos.value.push(novoTecnico);

      return novoTecnico;
    } catch (erro) {
      error.value = "Erro ao cadastrar técnico.";
      console.error(erro);
      throw erro;
    } finally {
      loading.value = false;
    }
  }

  async function atualizarTecnico(id, tecnicoAtualizado) {
    loading.value = true;
    error.value = null;

    const tecnicoRequest = {
      nome: tecnicoAtualizado.nome,
      telefone: tecnicoAtualizado.telefone,
      idsSetores: tecnicoAtualizado.idsSetores,
    };

    try {
      const resposta = await api.put(`/tecnicos/${id}`, tecnicoRequest);
      const tecnicoSalvo = normalizarTecnico(resposta.data);

      const index = tecnicos.value.findIndex(
        (tecnico) => tecnico.idTecnico === id,
      );

      if (index !== -1) {
        tecnicos.value[index] = tecnicoSalvo;
      }

      return tecnicoSalvo;
    } catch (erro) {
      error.value = "Erro ao atualizar técnico.";
      console.error(erro);
      throw erro;
    } finally {
      loading.value = false;
    }
  }

  return {
    tecnicos,
    loading,
    error,
    buscarTecnicos,
    buscarTecnicoPorId,
    adicionarTecnico,
    atualizarTecnico,
  };
});
