<template>
  <div class="pagina-tecnicos">
    <div class="cabecalho-pagina">
      <div>
        <h1>Técnicos</h1>
        <p>Gerencie os técnicos cadastrados no sistema.</p>
      </div>

      <button class="btn-cadastrar" @click="cadastrarTecnico">
        Cadastrar técnico
      </button>
    </div>

    <div class="card-filtros">
      <div class="campo-pesquisa">
        <label for="pesquisa">Pesquisar</label>
        <input
          id="pesquisa"
          v-model="pesquisa"
          type="text"
          placeholder="Buscar por nome, telefone ou setor..."
        />
      </div>

      <div class="campo-filtro">
        <label for="setor">Setor</label>
        <select id="setor" v-model="filtroSetor">
          <option value="">Todos os setores</option>
          <option
            v-for="setor in setoresDisponiveis"
            :key="setor"
            :value="setor"
          >
            {{ setor }}
          </option>
        </select>
      </div>
    </div>

    <div class="resumo-lista">
      <span>
        Total de técnicos: <strong>{{ tecnicosFiltrados.length }}</strong>
      </span>

      <span v-if="tecnicoStore.loading" class="status-carregando">
        Carregando técnicos...
      </span>

      <span v-if="tecnicoStore.error" class="status-erro">
        {{ tecnicoStore.error }}
      </span>
    </div>

    <div class="card-tabela">
      <table v-if="tecnicosFiltrados.length > 0">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Telefone</th>
            <th>Setores atendidos</th>
            <th class="coluna-acoes">Ações</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="tecnico in tecnicosFiltrados" :key="tecnico.idTecnico">
            <td>{{ tecnico.nome }}</td>
            <td>{{ tecnico.telefone }}</td>
            <td>{{ listarSetores(tecnico) }}</td>
            <td class="acoes">
              <button
                class="btn-editar"
                @click="editarTecnico(tecnico.idTecnico)"
              >
                Editar
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-else class="sem-registros">Nenhum técnico encontrado.</div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useTecnicosStore } from "@/stores/tecnicoStore";

const router = useRouter();
const tecnicoStore = useTecnicosStore();

const pesquisa = ref("");
const filtroSetor = ref("");

onMounted(async () => {
  await tecnicoStore.buscarTecnicos();
});

const setoresDisponiveis = computed(() => {
  const setores = tecnicoStore.tecnicos.flatMap((tecnico) =>
    tecnico.setoresAtendidos.map((setor) => setor.nomeSetor),
  );

  return [...new Set(setores)].filter(Boolean);
});

const tecnicosFiltrados = computed(() => {
  const termoPesquisa = pesquisa.value.toLowerCase().trim();

  return tecnicoStore.tecnicos.filter((tecnico) => {
    const nomesSetores = tecnico.setoresAtendidos
      .map((setor) => setor.nomeSetor)
      .join(" ")
      .toLowerCase();

    const correspondePesquisa =
      !termoPesquisa ||
      tecnico.nome?.toLowerCase().includes(termoPesquisa) ||
      tecnico.telefone?.toLowerCase().includes(termoPesquisa) ||
      nomesSetores.includes(termoPesquisa);

    const correspondeSetor =
      !filtroSetor.value ||
      tecnico.setoresAtendidos.some(
        (setor) => setor.nomeSetor === filtroSetor.value,
      );

    return correspondePesquisa && correspondeSetor;
  });
});

function listarSetores(tecnico) {
  if (!tecnico.setoresAtendidos || tecnico.setoresAtendidos.length === 0) {
    return "Nenhum setor vinculado";
  }

  return tecnico.setoresAtendidos.map((setor) => setor.nomeSetor).join(", ");
}

function cadastrarTecnico() {
  router.push("/tecnicos/cadastro");
}

function editarTecnico(id) {
  router.push(`/tecnicos/editar/${id}`);
}
</script>

<style scoped>
.pagina-tecnicos {
  width: 100%;
}

.cabecalho-pagina {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 24px;
}

.cabecalho-pagina h1 {
  font-size: 30px;
  color: #1e3a8a;
  margin-bottom: 6px;
}

.cabecalho-pagina p {
  color: #64748b;
  font-size: 15px;
}

.btn-cadastrar {
  border: none;
  background: #1e3a8a;
  color: #ffffff;
  padding: 12px 18px;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
}

.btn-cadastrar:hover {
  background: #1d4ed8;
}

.card-filtros {
  background: #ffffff;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 18px;
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.08);
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 16px;
}

.campo-pesquisa,
.campo-filtro {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.campo-pesquisa label,
.campo-filtro label {
  font-weight: 600;
  color: #334155;
  font-size: 14px;
}

.campo-pesquisa input,
.campo-filtro select {
  width: 100%;
  padding: 11px 13px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  font-size: 14px;
  outline: none;
  background: #ffffff;
}

.campo-pesquisa input:focus,
.campo-filtro select:focus {
  border-color: #2563eb;
}

.resumo-lista {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  color: #475569;
  font-size: 14px;
}

.status-carregando {
  color: #2563eb;
  font-weight: 600;
}

.status-erro {
  color: #dc2626;
  font-weight: 600;
}

.card-tabela {
  background: #ffffff;
  border-radius: 16px;
  padding: 18px;
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.08);
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: #f1f5f9;
}

th,
td {
  padding: 14px 12px;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
  color: #334155;
  font-size: 14px;
}

th {
  font-weight: 700;
  color: #1e293b;
}

.coluna-acoes {
  text-align: center;
}

.acoes {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.btn-editar {
  border: none;
  padding: 8px 11px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  background: #dbeafe;
  color: #1d4ed8;
}

.btn-editar:hover {
  background: #bfdbfe;
}

.sem-registros {
  padding: 28px;
  text-align: center;
  color: #64748b;
  font-size: 15px;
}

@media (max-width: 900px) {
  .cabecalho-pagina {
    flex-direction: column;
  }

  .card-filtros {
    grid-template-columns: 1fr;
  }

  .btn-cadastrar {
    width: 100%;
  }

  .resumo-lista {
    flex-direction: column;
    align-items: flex-start;
  }

  .acoes {
    flex-direction: column;
  }
}
</style>
