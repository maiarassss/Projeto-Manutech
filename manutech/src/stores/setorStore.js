import { ref } from "vue";
import { defineStore } from "pinia";
import { api } from "@/services/api";

export const useSetoresStore = defineStore("setores", () => {
  const setores = ref([]);
  const loading = ref(false);
  const error = ref(null);

  async function buscarSetores() {
    loading.value = true;
    error.value = null;

    try {
      const resposta = await api.get("/setores");
      setores.value = resposta.data;
      return resposta.data;
    } catch (erro) {
      error.value = "Erro ao buscar setores.";
      console.error(erro);
      return [];
    } finally {
      loading.value = false;
    }
  }

  return {
    setores,
    loading,
    error,
    buscarSetores,
  };
});
