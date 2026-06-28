<script setup>
import { Pencil, Trash2 } from "lucide-vue-next";
import { useTecnicosStore } from "@/stores/tecnicoStore";
import { onMounted, computed, ref } from "vue";

const store = useTecnicosStore();

const pesquisa = ref("");
const mostrarConfirmacao = ref(false);
const tecnicoParaExcluir = ref(null);

onMounted(() => {
  store.buscarTecnicos();
});

const tecnicos = computed(() => store.tecnicos);

function nomesSetores(setores) {
  return setores.map((setor) => setor.nomeSetor || setor.nome).join(", ");
}

const tecnicosFiltrados = computed(() => {
  return tecnicos.value.filter((tecnico) => {
    const termo = pesquisa.value.toLowerCase();

    const setores = nomesSetores(tecnico.setoresAtendidos).toLowerCase();

    return (
      !pesquisa.value ||
      tecnico.nome.toLowerCase().includes(termo) ||
      tecnico.telefone.toLowerCase().includes(termo) ||
      setores.includes(termo)
    );
  });
});

function confirmarExclusao(tecnico) {
  tecnicoParaExcluir.value = tecnico;
  mostrarConfirmacao.value = true;
}

async function excluirTecnico() {
  await store.excluirTecnico(tecnicoParaExcluir.value.idTecnico);

  mostrarConfirmacao.value = false;
  tecnicoParaExcluir.value = null;
}

function cancelarExclusao() {
  mostrarConfirmacao.value = false;
  tecnicoParaExcluir.value = null;
}

function limparPesquisa() {
  pesquisa.value = "";
}
</script>

<template>
  <div class="container">
    <h1>Técnicos</h1>

    <div class="barra-filtros">
      <input
        v-model="pesquisa"
        type="text"
        placeholder="Pesquisar por nome, telefone ou setor..."
      />

      <button class="btn-limpar" @click="limparPesquisa">
        Limpar pesquisa
      </button>
    </div>

    <div class="contador">
      {{ tecnicosFiltrados.length }} técnicos encontrados
    </div>

    <table class="tabela-tecnicos">
      <thead>
        <tr>
          <th>Nome</th>
          <th>Telefone</th>
          <th>Setores atendidos</th>
          <th>Ações</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="tecnico in tecnicosFiltrados" :key="tecnico.idTecnico">
          <td>{{ tecnico.nome }}</td>
          <td>{{ tecnico.telefone }}</td>
          <td>{{ nomesSetores(tecnico.setoresAtendidos) }}</td>

          <td class="acoes">
            <router-link :to="`/tecnicos/editar/${tecnico.idTecnico}`">
              <button class="editar" type="button">
                <Pencil :size="18" />
              </button>
            </router-link>

            <button
              class="excluir"
              type="button"
              @click="confirmarExclusao(tecnico)"
            >
              <Trash2 :size="18" />
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <router-link to="/tecnicos/cadastro">
      <button class="btn-cadastro">+ Cadastrar Técnico</button>
    </router-link>

    <div v-if="mostrarConfirmacao" class="overlay">
      <div class="modal">
        <h2>Confirmar exclusão</h2>

        <p>
          Deseja realmente excluir o técnico
          <strong>{{ tecnicoParaExcluir?.nome }}</strong
          >?
        </p>

        <div class="botoes-modal">
          <button class="cancelar" @click="cancelarExclusao">Cancelar</button>

          <button class="confirmar" @click="excluirTecnico">Excluir</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 0;

  display: flex;
  flex-direction: column;
  align-items: center;
}

h1 {
  color: #153b7a;
  font-size: 3rem;
  margin-bottom: 30px;
}

.barra-filtros {
  width: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;

  margin-bottom: 25px;

  flex-wrap: wrap;
}

.barra-filtros input {
  padding: 12px;
  min-width: 350px;

  border: 1px solid #d8d8d8;
  border-radius: 8px;

  font-size: 1rem;
}

.btn-limpar {
  background: #173f82;
  color: white;

  padding: 12px 24px;

  border: none;
  border-radius: 8px;

  cursor: pointer;

  transition: 0.2s;
}

.btn-limpar:hover {
  background: #123063;
}

.contador {
  width: 100%;
  margin-bottom: 15px;

  color: #555;
  font-size: 1rem;
  font-weight: bold;
}

.tabela-tecnicos {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
}

.tabela-tecnicos thead {
  background: #173f82;
  color: white;
}

.tabela-tecnicos th,
.tabela-tecnicos td {
  padding: 18px;
  text-align: center;
}

.tabela-tecnicos tbody tr {
  border-bottom: 1px solid #ececec;
}

.tabela-tecnicos tbody tr:hover {
  background: #f7f9fc;
}

.acoes {
  display: flex;
  justify-content: center;
  gap: 15px;
}

button {
  border: none;
  background: transparent;
  cursor: pointer;
}

.editar {
  color: #173f82;
}

.excluir {
  color: #d62828;
}

.btn-cadastro {
  margin-top: 35px;
  width: 280px;
  height: 60px;

  background: #173f82;
  color: white;

  border-radius: 8px;

  font-size: 1.1rem;
  font-weight: bold;

  transition: 0.2s;
}

.btn-cadastro:hover {
  background: #123063;
}

.overlay {
  position: fixed;
  inset: 0;

  background: rgba(0, 0, 0, 0.45);

  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: white;

  width: 400px;

  padding: 30px;

  border-radius: 12px;

  text-align: center;

  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
}

.modal h2 {
  color: #173f82;
}

.botoes-modal {
  display: flex;
  justify-content: center;
  gap: 20px;

  margin-top: 25px;
}

.cancelar,
.confirmar {
  padding: 12px 25px;

  border-radius: 8px;

  font-weight: bold;

  cursor: pointer;
}

.cancelar {
  background: #ddd;
  color: #333;
}

.confirmar {
  background: #d62828;
  color: white;
}
</style>
