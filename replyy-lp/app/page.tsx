import { cookies } from "next/headers";
import { WaitlistForm } from "@/components/WaitlistForm";
import { COOKIE_NAME, pickVariant, type HeroVariant } from "@/lib/variants";

export default async function Home() {
  const cookieStore = await cookies();
  const variant = pickVariant(cookieStore.get(COOKIE_NAME)?.value);

  return (
    <main className="min-h-screen bg-paper text-ink">
      <Nav />
      <Hero variant={variant} />
      <FutureYouHad />
      <MorningYouChose />
      <NinetySecond />
      <NotCalendly />
      <WhoLost />
      <VoiceThatWasYours />
      <WhatYouDontLose />
      <Pricing />
      <FAQ />
      <FinalCTA />
      <Footer />
    </main>
  );
}

function Nav() {
  return (
    <header className="container-prose flex items-center justify-between py-6">
      <a href="#top" className="text-lg font-semibold tracking-tight">
        Replyy<span className="text-accent">.</span>
      </a>
      <nav className="hidden gap-8 text-sm text-ink-muted sm:flex">
        <a href="#lost" className="hover:text-ink">The morning</a>
        <a href="#how" className="hover:text-ink">How</a>
        <a href="#pricing" className="hover:text-ink">Pricing</a>
        <a href="#faq" className="hover:text-ink">FAQ</a>
      </nav>
      <a href="#cta" className="btn-ghost px-4 py-2 text-sm">Take it back</a>
    </header>
  );
}

function Hero({ variant }: { variant: HeroVariant }) {
  return (
    <section id="top" className="container-prose pt-12 pb-24 sm:pt-20 sm:pb-32">
      <p className="eyebrow mb-6">{variant.eyebrow}</p>
      <h1 className="font-serif text-5xl leading-[1.05] tracking-tight sm:text-6xl md:text-7xl">
        {variant.headline}
        <br />
        <span className="text-ink-muted">{variant.headlineMuted}</span>
      </h1>
      <p className="mt-8 max-w-prose text-lg leading-relaxed text-ink-soft sm:text-xl">
        {variant.sub}
      </p>
      <div className="mt-10 max-w-xl">
        <WaitlistForm />
        <p className="mt-3 text-sm text-ink-muted">
          Founding price <strong className="text-ink">$9/mo</strong>, locked for
          life. The window closes at 100 members.
        </p>
      </div>
    </section>
  );
}

function FutureYouHad() {
  return (
    <section id="future" className="border-t border-ink/10 bg-white py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">The future you already had</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          It’s 9:14 AM on a Tuesday.
          <br />
          <span className="text-ink-muted">The morning is already yours.</span>
        </h2>
        <div className="mt-10 max-w-prose space-y-6 text-lg leading-relaxed text-ink-soft">
          <p>
            The Hartmann email came in at 8:47. The one worth{" "}
            <strong className="text-ink">$180,000</strong> if you close it. You’re
            in the kitchen. The light is the kind of pale you only get before 10
            AM in October. You glance at your phone, read the email once, and tap
            a single button.
          </p>
          <p>
            By the time the espresso is done, three drafts are open on your
            laptop. The second one reads like you wrote it on a quiet Tuesday
            morning — your hedge in the second sentence, your sign-off, three
            real time slots pulled live from your calendar.{" "}
            <em>Tuesday 2pm. Wednesday 10. Thursday 4.</em> No paragraph of
            scheduling logic. No “let me know what works.” One copy, one paste,
            one send.
          </p>
          <p>
            <strong className="text-ink">9:16 AM.</strong> Total thinking time:
            ninety seconds. You walk into your 9:30 with one less item in your
            head and the second-largest deal of the year already moving.
          </p>
          <p className="text-ink">
            <strong>This Tuesday is already in your calendar.</strong> You just
            haven’t unlocked it.
          </p>
        </div>
      </div>
    </section>
  );
}

function MorningYouChose() {
  return (
    <section id="lost" className="border-t border-ink/10 py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">The morning you keep choosing</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          It’s still 9:14 AM.
          <br />
          <span className="text-ink-muted">The email is still there.</span>
        </h2>
        <div className="mt-10 max-w-prose space-y-6 text-lg leading-relaxed text-ink-soft">
          <p>
            You read the Hartmann email a third time. You alt-tab to your
            calendar. You type{" "}
            <em>“Tuesday or Thursday work, happy to —”</em> and then you delete
            it because it sounds like every other AE on Earth. You start again.
            You think about whether to propose Wednesday because they’re East
            Coast. You re-read the original. You hedge.
          </p>
          <p>
            You send at <strong className="text-ink">10:08 AM</strong>. Fifty-four
            minutes lost. The 9:30 meeting just ate its own preparation window. By
            5 PM you’ll have done this four more times.
          </p>
          <p>
            This Tuesday looks identical to last Tuesday. By December, you’ve
            written the same six-sentence scheduling email{" "}
            <strong className="text-ink">564 times</strong>. Three full workdays
            of typing, gone. The Hartmann deal? Your competitor in Atlanta
            replied at 9:05 with three times in a single line of warmth. The
            prospect booked Thursday with them. You’ll find out in February when
            their logo appears on a press release.
          </p>
          <p className="text-ink">
            <strong>
              This is the morning you chose. You’ll choose it again tomorrow.
            </strong>{" "}
            Not because you want to. Because the alternative — sitting down to
            craft a personal scheduling email forty-seven times a month — is not
            a thing a human does at scale.
          </p>
        </div>
      </div>
    </section>
  );
}

function NinetySecond() {
  const steps = [
    {
      n: "01",
      title: "Paste their email",
      body:
        "Drop in the message you just received. Replyy reads the tone — formal, warm, urgent, hedging — and the meeting context.",
    },
    {
      n: "02",
      title: "Pick the one that sounds like you",
      body:
        "Three drafts. Each in your voice. Each with three real time slots from your calendar. The one you’d have written if you had the morning back.",
    },
    {
      n: "03",
      title: "Send from your own inbox",
      body:
        "Copy. Paste. Hit send. We never touch your outbox. Your prospect sees an email from you — because that’s what it is.",
    },
  ];
  return (
    <section id="how" className="border-t border-ink/10 bg-white py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">How you take it back</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          Ninety seconds.
          <br />
          <span className="text-ink-muted">
            Not three workdays a month.
          </span>
        </h2>
        <p className="mt-6 max-w-prose text-lg leading-relaxed text-ink-soft">
          Replyy isn’t a faster scheduling link. It’s the Tuesday morning you
          had before scheduling links existed — the one where you wrote three
          lines and won the deal.
        </p>
        <div className="mt-12 grid gap-6 sm:grid-cols-3">
          {steps.map((s) => (
            <div
              key={s.n}
              className="rounded-2xl border border-ink/10 bg-paper p-6"
            >
              <p className="font-serif text-2xl text-accent">{s.n}</p>
              <h3 className="mt-3 text-lg font-semibold">{s.title}</h3>
              <p className="mt-2 text-base leading-relaxed text-ink-soft">
                {s.body}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function NotCalendly() {
  const rows: Array<[string, string, string]> = [
    [
      "What your prospect feels",
      "“I’m one of forty in their pipeline.”",
      "“They made time for me.”",
    ],
    [
      "Average reply time",
      "Same — but feels colder",
      "Eighteen minutes, in your voice",
    ],
    [
      "Where the deal goes",
      "“Vendor” folder. Pricing comparison.",
      "Partner conversation. No comparison.",
    ],
    [
      "Calendar integration",
      "Yes",
      "Yes — same Google/Outlook backend",
    ],
    ["Price", "$12–$20/mo", "$9/mo founding price, locked for life"],
  ];
  return (
    <section className="border-t border-ink/10 py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">Calendly didn’t fail you</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          Your prospect’s patience did.
          <br />
          <span className="text-ink-muted">
            The link wasn’t the problem until it was.
          </span>
        </h2>
        <p className="mt-6 max-w-prose text-lg leading-relaxed text-ink-soft">
          Calendly is excellent — for the part of your pipeline that doesn’t
          care if you’re one of forty. Replyy is for the part where{" "}
          <em>that distinction is the deal</em>.
        </p>
        <div className="mt-12 overflow-hidden rounded-2xl border border-ink/10">
          <table className="w-full text-left">
            <thead className="bg-ink text-paper">
              <tr>
                <th className="px-6 py-4 text-sm font-medium"></th>
                <th className="px-6 py-4 text-sm font-medium">Calendly</th>
                <th className="px-6 py-4 text-sm font-medium">Replyy</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-ink/10 bg-paper">
              {rows.map(([label, a, b]) => (
                <tr key={label}>
                  <td className="px-6 py-4 text-sm font-medium text-ink-muted">
                    {label}
                  </td>
                  <td className="px-6 py-4 text-base">{a}</td>
                  <td className="px-6 py-4 text-base font-medium">{b}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </section>
  );
}

function WhoLost() {
  const cards = [
    {
      title: "Enterprise AEs",
      body: "You closed 14 deals last year. You worked on 47. Of the 33 that didn’t close, ask how many died between “great call” and “let me know what time works.”",
    },
    {
      title: "Executive coaches",
      body: "Your average client is worth $48k over twelve months. They decide whether you’re “a coach” or “the coach” in the first email. The Calendly link gives them the answer.",
    },
    {
      title: "Boutique consultants",
      body: "Your last three lost engagements all said the same thing in the post-mortem: “It just didn’t feel like the right fit.” Half of that feeling was your scheduling email.",
    },
    {
      title: "Lawyers & advisors",
      body: "You bill $850 an hour. The five minutes you spent re-writing a scheduling email last Tuesday cost more than this subscription costs all year.",
    },
  ];
  return (
    <section className="border-t border-ink/10 bg-white py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">Who already lost a deal this way</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          If your average deal is over $25k,
          <br />
          <span className="text-ink-muted">
            this has already happened to you.
          </span>
        </h2>
        <p className="mt-6 max-w-prose text-lg leading-relaxed text-ink-soft">
          You probably can’t name the deal. That’s the point. Scheduling friction
          loses deals quietly — they don’t show up in the lost-reason field of
          your CRM. They show up in the gap between the 47 conversations and the
          14 closes.
        </p>
        <div className="mt-12 grid gap-6 sm:grid-cols-2">
          {cards.map((c) => (
            <div
              key={c.title}
              className="rounded-2xl border border-ink/10 bg-paper p-6"
            >
              <h3 className="text-lg font-semibold">{c.title}</h3>
              <p className="mt-2 text-base leading-relaxed text-ink-soft">
                {c.body}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function VoiceThatWasYours() {
  return (
    <section className="border-t border-ink/10 py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">The voice that was already yours</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          It learns from five emails.
          <br />
          <span className="text-ink-muted">
            Then it disappears into the work.
          </span>
        </h2>
        <div className="mt-10 max-w-prose space-y-6 text-lg leading-relaxed text-ink-soft">
          <p>
            Onboarding is forty seconds. Forward five emails you’ve sent —
            preferably to people who matter. Replyy extracts your sentence
            length, your sign-offs, the hedges only you use, your exact warmth.
            Then it disappears into the work.
          </p>
          <p>
            No em-dashes you’d never write. No “I hope this finds you well” if
            you’ve never written one in your life. No tracking pixels, no read
            receipts, no “powered by.” The output is{" "}
            <strong className="text-ink">your email</strong>. The only
            difference is you wrote it in ninety seconds instead of nine
            minutes.
          </p>
          <p>
            You’ve been writing this email correctly since the day you started
            closing big deals. The version you wrote on quiet mornings was
            already optimal. You just couldn’t keep doing it 47 times a month.
          </p>
        </div>
      </div>
    </section>
  );
}

function WhatYouDontLose() {
  const items = [
    {
      title: "Your inbox",
      body: "We never send. You copy, paste, hit send yourself. Your SMTP, your outbox, your audit trail.",
    },
    {
      title: "Your calendar privacy",
      body: "Read-only access. We see your busy blocks. We never see event titles, attendees, or notes.",
    },
    {
      title: "Your drafts",
      body: "Auto-deleted after 30 days. Or instantly, from your dashboard. Default off for any training.",
    },
  ];
  return (
    <section className="border-t border-ink/10 bg-white py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">What you don’t lose</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          We take back the morning.
          <br />
          <span className="text-ink-muted">Nothing else leaves your control.</span>
        </h2>
        <div className="mt-12 grid gap-6 sm:grid-cols-3">
          {items.map((i) => (
            <div
              key={i.title}
              className="rounded-2xl border border-ink/10 bg-paper p-6"
            >
              <h3 className="text-base font-semibold">{i.title}</h3>
              <p className="mt-2 text-base leading-relaxed text-ink-soft">
                {i.body}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function Pricing() {
  return (
    <section id="pricing" className="border-t border-ink/10 py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">Pricing</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          $9/mo, locked for life.
          <br />
          <span className="text-ink-muted">
            Or $19/mo, like everyone after you.
          </span>
        </h2>
        <p className="mt-6 max-w-prose text-lg leading-relaxed text-ink-soft">
          We’re taking 100 founding members at the launch price. When the
          hundredth one signs up, the window closes — for everyone. After that,
          the price is $19/mo and the lock is gone. The members who got in keep
          their $9 forever.
        </p>
        <div className="mt-12 flex justify-center">
          <div className="w-full max-w-md rounded-3xl border-2 border-ink bg-paper p-8">
            <p className="eyebrow text-accent">Founding member</p>
            <p className="mt-4 font-serif text-5xl tracking-tight">
              $9<span className="text-2xl text-ink-muted">/month</span>
            </p>
            <p className="mt-1 text-sm text-ink-muted">
              normally $19. Locked for life as long as you stay subscribed.
            </p>
            <ul className="mt-6 space-y-2 text-base">
              <li>· Unlimited drafts</li>
              <li>· Google Calendar &amp; Outlook</li>
              <li>· 5-email voice training</li>
              <li>· Cancel anytime — you just lose the price</li>
            </ul>
            <div className="mt-6">
              <WaitlistForm variant="secondary" />
              <p className="mt-3 text-xs text-ink-muted">
                No charge today. We launch in June 2026. You’ll be invited two
                weeks early.
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

function FAQ() {
  const qs = [
    {
      q: "Isn’t this just GPT with a calendar plugin?",
      a: "The calendar part is two days of work. The hard part — the only part that matters — is making the reply sound like you wrote it. Not like “Sure, happy to chat! Here are some times!” That’s where 80% of our engineering goes. If you can’t tell it apart from your own writing in five emails, we haven’t shipped yet.",
    },
    {
      q: "Will my prospect know it’s AI?",
      a: "Only if you tell them. No link. No widget. No tracking pixel. No “powered by.” It’s an email you reviewed, edited if you wanted, and sent from your own inbox. Because that’s what it is.",
    },
    {
      q: "What if it sounds wrong?",
      a: "You don’t send it. You get three drafts every time. Pick one, edit it, or write your own — the same way you’d treat a draft from an assistant who knows your voice. The difference is the assistant doesn’t cost $60k/year.",
    },
    {
      q: "Languages?",
      a: "English at launch. Japanese support in v1.1 (Q4 2026). If your buyers reply in English, you’re covered now.",
    },
    {
      q: "When does the founding price actually close?",
      a: "When 100 people sign up. We’re not playing scarcity games — there’s a real Stripe limit and you’ll see the count on the next email we send. After that, the price is $19/mo and there’s no lock.",
    },
  ];
  return (
    <section id="faq" className="border-t border-ink/10 bg-white py-24">
      <div className="container-prose">
        <p className="eyebrow mb-4">FAQ</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          Questions, mostly answered.
        </h2>
        <div className="mt-12 divide-y divide-ink/10 border-y border-ink/10">
          {qs.map((item) => (
            <details key={item.q} className="group py-6">
              <summary className="flex cursor-pointer list-none items-start justify-between gap-6 text-lg font-medium">
                {item.q}
                <span className="mt-1 text-ink-muted transition group-open:rotate-45">
                  +
                </span>
              </summary>
              <p className="mt-4 max-w-prose text-base leading-relaxed text-ink-soft">
                {item.a}
              </p>
            </details>
          ))}
        </div>
      </div>
    </section>
  );
}

function FinalCTA() {
  return (
    <section id="cta" className="border-t border-ink/10 bg-ink py-24 text-paper">
      <div className="container-prose text-center">
        <p className="eyebrow mb-4 text-paper/60">One decision</p>
        <h2 className="font-serif text-4xl leading-tight tracking-tight sm:text-5xl">
          The next 47 emails are coming.
          <br />
          <span className="text-paper/60">
            Decide once whether you write them, or Replyy does.
          </span>
        </h2>
        <p className="mx-auto mt-6 max-w-prose text-lg text-paper/70">
          Every Tuesday morning you spend on a six-sentence email is a Tuesday
          morning you’re not spending on the call that closes the deal. Take
          back the morning. Lock the $9 price. Launch in three weeks.
        </p>
        <div className="mx-auto mt-10 max-w-xl">
          <WaitlistForm />
        </div>
        <p className="mt-4 text-sm text-paper/50">
          47 founding spots left of 100. No charge today.
        </p>
      </div>
    </section>
  );
}

function Footer() {
  return (
    <footer className="container-prose flex flex-col items-start justify-between gap-4 py-12 text-sm text-ink-muted sm:flex-row">
      <p>&copy; {new Date().getFullYear()} Replyy. Built solo.</p>
      <div className="flex gap-6">
        <a href="mailto:hello@replyy.com" className="hover:text-ink">
          hello@replyy.com
        </a>
        <a href="#privacy" className="hover:text-ink">
          Privacy
        </a>
      </div>
    </footer>
  );
}
