"use client";

import React from "react";
import NavigationSidebar from "./components/navigation/NavigationSidebar";
import GatheringSidebar from "./components/gathering/GatheringSidebar";

const layout = ({ children }: { children: React.ReactNode }) => {
  return (
    <div className="h-full">
      <div className="hidden md:flex h-full w-60 z-20 flex-col fixed inset-y-0">
        <GatheringSidebar />
      </div>
      <main className="h-full md:pl-60">{children}</main>
    </div>
  );
};

export default layout;
