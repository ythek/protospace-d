"use client"

import PrototypeDetail, { PrototypeData } from "@/app/components/PrototypeDetail";
import { useEffect, useState } from "react";

const Backend_url= "http://localhost:8080";




export default function PrototypeDetailPage() {
  const [prototype, setPrototype]= useState<PrototypeData | null>(null);

  useEffect(() => {
    fetch(`${Backend_url}/prototype`)
    .then((res) => res.json())
    .then((data: PrototypeData) => setPrototype(data));
      }, []);
    if (!prototype) return null;

  return <PrototypeDetail prototype={prototype} />;
}