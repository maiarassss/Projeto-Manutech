import { computed, ref } from "vue";
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", () => {
  const usuarioLogado = ref(
    JSON.parse(localStorage.getItem("usuarioLogado")) || null,
  );

  const estaLogado = computed(() => !!usuarioLogado.value);

  const perfil = computed(() => usuarioLogado.value?.perfil || null);

  const ehGestor = computed(() => perfil.value === "GESTOR");

  const ehTecnico = computed(() => perfil.value === "TECNICO");

  function salvarUsuario(usuario) {
    usuarioLogado.value = usuario;
    localStorage.setItem("usuarioLogado", JSON.stringify(usuario));
  }

  function sair() {
    usuarioLogado.value = null;
    localStorage.removeItem("usuarioLogado");
  }

  return {
    usuarioLogado,
    estaLogado,
    perfil,
    ehGestor,
    ehTecnico,
    salvarUsuario,
    sair,
  };
});
