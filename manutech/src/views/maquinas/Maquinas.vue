<script setup>
import { ref } from "vue";
import { Pencil, Trash2 } from "lucide-vue-next";

const maquinas = ref([
  { id: 1, nome: "Máquina A" },
  { id: 2, nome: "Máquina B" },
  { id: 3, nome: "Máquina C" },
]);

function excluirMaquina(id) {
  maquinas.value = maquinas.value.filter(maquina => maquina.id !== id);
}

</script>
<template>
  <div class="container">
    <h1>Máquinas</h1>

    <div class="maquina-item" v-for="maquina in maquinas" :key="maquina.id">
      <span>{{ maquina.nome }}</span>

      <div class="acoes">
        <router-link :to="`/maquinas/editar/${maquina.id}`">
          <button class="editar" type="button">
            <Pencil :size="18" />
          </button>
        </router-link>
        
        <button class="excluir" type="button" @click="excluirMaquina(maquina.id)">
          <Trash2 :size="18" />
        </button>
      </div>
    </div>
    
    <router-link to="/maquinas/cadastro">
  <button class="btn-cadastro">
    + Cadastrar Máquina
  </button>
</router-link>
  
</div>
</template>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 0px; 
}

h1 {
    color: #153b7a;
    font-size: 3rem;
    margin-bottom: 10px; 
}

.maquina-item {
    width: 60%;
    background: white;
    border: 1px solid #dcdcdc;
    border-radius: 8px;

    display: flex;
    justify-content: space-between;
    align-items: center;

    padding: 18px 24px;
    margin-bottom: 20px;

    box-shadow: 0 2px 5px rgba(0,0,0,.08);
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
</style>
