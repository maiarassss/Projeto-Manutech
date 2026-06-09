import { createRouter, createWebHistory } from "vue-router";

import InicioView from "../views/Inicio.vue";
import OrdensView from "../views/Ordens.vue";
import TecnicosView from "../views/Tecnicos.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      component: InicioView,
    },
    {
      path: "/ordens",
      component: OrdensView,
    },
    {
      path: "/tecnicos",
      component: TecnicosView,
    },
  ],
});

export default router;
