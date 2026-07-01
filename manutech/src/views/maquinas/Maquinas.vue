<template>
  <div class="pagina-maquinas">
    <div class="cabecalho-pagina">
      <div>
        <h1>Máquinas</h1>
        <p>Gerencie as máquinas industriais cadastradas no sistema.</p>
      </div>

      <button class="btn-cadastrar" @click="cadastrarMaquina">
        Cadastrar máquina
      </button>
    </div>

    <div class="card-filtros">
      <div class="campo-pesquisa">
        <label for="pesquisa">Pesquisar</label>
        <input
          id="pesquisa"
          v-model="pesquisa"
          type="text"
          placeholder="Buscar por código, modelo ou setor..."
        />
      </div>

      <div class="campo-filtro">
        <label for="modelo">Modelo</label>
        <select id="modelo" v-model="filtroModelo">
          <option value="">Todos os modelos</option>
          <option
            v-for="modelo in modelosDisponiveis"
            :key="modelo"
            :value="modelo"
          >
            {{ modelo }}
          </option>
        </select>
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
        Total de máquinas: <strong>{{ maquinasFiltradas.length }}</strong>
      </span>

      <span v-if="maquinaStore.loading" class="status-carregando">
        Carregando máquinas...
      </span>

      <span v-if="maquinaStore.error" class="status-erro">
        {{ maquinaStore.error }}
      </span>
    </div>

    <div class="card-tabela">
      <table v-if="maquinasFiltradas.length > 0">
        <thead>
          <tr>
            <th>Código</th>
            <th>Modelo</th>
            <th>Setor</th>
            <th>Status</th>
            <th class="coluna-acoes">Ações</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="maquina in maquinasFiltradas" :key="maquina.idMaquina">
            <td>{{ maquina.codigoIdentificador }}</td>
            <td>{{ maquina.modelo }}</td>
            <td>{{ maquina.nomeSetor }}</td>
            <td>
              <span
                class="badge-status"
                :class="maquina.ativa ? 'ativa' : 'inativa'"
              >
                {{ maquina.ativa ? "Ativa" : "Inativa" }}
              </span>
            </td>
            <td class="acoes">
              <button
                class="btn-editar"
                @click="editarMaquina(maquina.idMaquina)"
              >
                Editar
              </button>

              <button
                v-if="maquina.ativa"
                class="btn-inativar"
                @click="inativarMaquina(maquina)"
              >
                Inativar
              </button>

              <span v-else class="texto-inativa"> Já inativa </span>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-else class="sem-registros">Nenhuma máquina encontrada.</div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useMaquinasStore } from "@/stores/maquinaStore";

const router = useRouter();
const maquinaStore = useMaquinasStore();

const pesquisa = ref("");
const filtroModelo = ref("");
const filtroSetor = ref("");

onMounted(async () => {
  await maquinaStore.buscarMaquinas();
});

const modelosDisponiveis = computed(() => {
  const modelos = maquinaStore.maquinas.map((maquina) => maquina.modelo);
  return [...new Set(modelos)].filter(Boolean);
});

const setoresDisponiveis = computed(() => {
  const setores = maquinaStore.maquinas.map((maquina) => maquina.nomeSetor);
  return [...new Set(setores)].filter(Boolean);
});

const maquinasFiltradas = computed(() => {
  const termoPesquisa = pesquisa.value.toLowerCase().trim();

  return maquinaStore.maquinas.filter((maquina) => {
    const correspondePesquisa =
      !termoPesquisa ||
      maquina.codigoIdentificador?.toLowerCase().includes(termoPesquisa) ||
      maquina.modelo?.toLowerCase().includes(termoPesquisa) ||
      maquina.nomeSetor?.toLowerCase().includes(termoPesquisa);

    const correspondeModelo =
      !filtroModelo.value || maquina.modelo === filtroModelo.value;

    const correspondeSetor =
      !filtroSetor.value || maquina.nomeSetor === filtroSetor.value;

    return correspondePesquisa && correspondeModelo && correspondeSetor;
  });
});

function cadastrarMaquina() {
  router.push("/maquinas/cadastro");
}

function editarMaquina(id) {
  router.push(`/maquinas/editar/${id}`);
}

async function inativarMaquina(maquina) {
  const confirmar = confirm("Deseja realmente inativar esta máquina?");

  if (!confirmar) {
    return;
  }

  try {
    await maquinaStore.atualizarMaquina(maquina.idMaquina, {
      codigoIdentificador: maquina.codigoIdentificador,
      modelo: maquina.modelo,
      ativa: false,
      idSetor: maquina.idSetor,
    });

    await maquinaStore.buscarMaquinas();
  } catch (erro) {
    console.error(erro);
    alert("Não foi possível inativar a máquina.");
  }
}
</script>

<style scoped>
.pagina-maquinas {
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
  grid-template-columns: 2fr 1fr 1fr;
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

.badge-status {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
}

.ativa {
  background: #dcfce7;
  color: #166534;
}

.inativa {
  background: #fee2e2;
  color: #991b1b;
}

.btn-editar,
.btn-inativar {
  border: none;
  padding: 8px 11px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
}

.btn-editar {
  background: #dbeafe;
  color: #1d4ed8;
}

.btn-inativar {
  background: #fee2e2;
  color: #b91c1c;
}

.btn-editar:hover {
  background: #bfdbfe;
}

.btn-inativar:hover {
  background: #fecaca;
}

.texto-inativa {
  color: #64748b;
  font-size: 13px;
  font-weight: 600;
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
