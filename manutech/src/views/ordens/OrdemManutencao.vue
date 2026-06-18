<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2>Ordens de Manutenção</h2>

      <RouterLink to="/ordens/nova" class="btn btn-dark">
        + Nova Ordem
      </RouterLink>
    </div>

    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
      <div class="col" v-for="ordem in ordens" :key="ordem.id">
        <OrdemManutencaoCard :ordem="ordem" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";

import OrdemManutencaoCard from "@/components/Ordem/OrdemCard.vue";

import { getOrdensManutencao, getMaquinas } from "../services/api";

import type {
  OrdemManutencao,
  OrdemManutencaoDetalhes,
} from "../interfaces/OrdemManutencao";

import type Maquina from "../interfaces/Maquina";

const ordens = ref<OrdemManutencaoDetalhes[]>([]);

async function buscarOrdensEMaquinas() {
  const resOrdens = await getOrdensManutencao();
  const resMaquinas = await getMaquinas();

  const listaOrdens: OrdemManutencao[] = resOrdens;
  const listaMaquinas: Maquina[] = resMaquinas;

  for (let i = 0; i < listaOrdens.length; i++) {
    const ordem = listaOrdens[i];

    const maquina = listaMaquinas.find((m) => m.id == ordem.maquinaId);

    const ordemDetalhes = {
      ...ordem,
      nomeMaquina: maquina?.nome,
      setor: maquina?.setor,
    } as OrdemManutencaoDetalhes;

    ordens.value.push(ordemDetalhes);
  }
}

onMounted(buscarOrdensEMaquinas);
</script>
