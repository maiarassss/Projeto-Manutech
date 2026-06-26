<template>
  <h1>Ordens de Manutenção</h1>
</template>

<!-- <script setup>
import { Pencil, Trash2 } from "lucide-vue-next";
import { useOrdensManutencaoStore } from "@/stores/ordensManutencaoStore"
import { onMounted } from "vue"
import { ordensManutencao } from "../../stores/ordensManutencao.js";
const store = useOrdensManutencaoStore()

onMounted(() => {
  //aguardando backend
  })

function excluirOrdem(id) {
  ordensManutencao.value = ordensManutencao.value.filter(
    ordem => ordem.id !== id
  );
}/*  */
</script>

<template>
  <div class="container">
    <h1>Ordens de Manutenção</h1>

    <div
      class="ordem-item"
      v-for="ordem in ordensManutencao"
      :key="ordem.id"
    >
      <span>{{ ordem.titulo }}</span>

      <div class="acoes">
        <router-link :to="`/ordens-manutencao/editar/${ordem.id}`">
          <button class="editar" type="button">
            <Pencil :size="18" />
          </button>
        </router-link>

        <button
          class="excluir"
          type="button"
          @click="excluirOrdem(ordem.id)"
        >
          <Trash2 :size="18" />
        </button>
      </div>
    </div>

    <router-link to="/ordens-manutencao/cadastro">
      <button class="btn-cadastro">
        + Cadastrar Ordem de Manutenção
      </button>
    </router-link>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 0;
}

h1 {
  color: #153b7a;
  font-size: 3rem;
  margin-bottom: 10px;
}

.ordem-item {
  width: 60%;
  background: white;
  border: 1px solid #dcdcdc;
  border-radius: 8px;

  display: flex;
  justify-content: space-between;
  align-items: center;

  padding: 18px 24px;
  margin-bottom: 20px;

  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.08);
}

.acoes {
  display: flex;
  gap: 30px;
}

.editar {
  color: blue;
}

.excluir {
  color: red;
}

button {
  border: none;
  background: none;
  cursor: pointer;
}

.btn-cadastro {
  margin-top: 50px;
  width: 300px;
  height: 70px;

  background: #173f82;
  color: white;

  border: none;
  border-radius: 10px;

  font-size: 1.4rem;
  font-weight: bold;

  cursor: pointer;
}

.btn-cadastro:hover {
  background-color: #17305b;
}
</style> -->